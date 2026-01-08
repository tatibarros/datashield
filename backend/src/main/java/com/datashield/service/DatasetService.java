package com.datashield.service;

import com.datashield.domain.Dataset;
import com.datashield.domain.User;
import com.datashield.dto.DatasetDTO;
import com.datashield.repository.DatasetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatasetService {
    
    private final DatasetRepository datasetRepository;
    private final AuditService auditService;
    
    @Value("${app.datastorage.path:./data}")
    private String storageBasePath;

    public DatasetDTO uploadDataset(MultipartFile file, String name, User owner) {
        try {
            String storagePath = Paths.get(storageBasePath, "datasets").toString();
            Files.createDirectories(Paths.get(storagePath));
            
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = Paths.get(storagePath, fileName).toString();
            
            Files.write(Paths.get(filePath), file.getBytes());
            
            long rowCount = estimateRowCount(file);
            List<String> columns = new ArrayList<>();
            
            if (file.getOriginalFilename().endsWith(".csv")) {
                columns = extractCsvHeaders(file);
            }
            
            Dataset dataset = Dataset.builder()
                    .name(name)
                    .filePath(filePath)
                    .fileType(file.getContentType() != null ? file.getContentType() : "text/plain")
                    .owner(owner)
                    .rowCount(rowCount)
                    .columnCount(columns.size())
                    .columns(columns)
                    .build();
            
            Dataset saved = datasetRepository.save(dataset);
            auditService.logDatasetUpload(owner, saved.getId(), name);
            
            log.info("Dataset uploaded: {} by {}", name, owner.getUsername());
            return mapToDTO(saved);
        } catch (IOException e) {
            log.error("Error uploading dataset: {}", e.getMessage());
            throw new RuntimeException("Failed to upload dataset", e);
        }
    }

    public List<DatasetDTO> listDatasets(User owner) {
        return datasetRepository.findByOwnerOrderByCreatedAtDesc(owner)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DatasetDTO getDataset(Long id) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));
        return mapToDTO(dataset);
    }

    public void deleteDataset(Long id) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dataset not found"));
        
        try {
            Files.deleteIfExists(Paths.get(dataset.getFilePath()));
        } catch (IOException e) {
            log.warn("Could not delete file: {}", dataset.getFilePath());
        }
        
        datasetRepository.delete(dataset);
    }

    private DatasetDTO mapToDTO(Dataset dataset) {
        return DatasetDTO.builder()
                .id(dataset.getId())
                .name(dataset.getName())
                .fileType(dataset.getFileType())
                .rowCount(dataset.getRowCount())
                .columnCount(dataset.getColumnCount())
                .columns(dataset.getColumns())
                .ownerUsername(dataset.getOwner().getUsername())
                .createdAt(dataset.getCreatedAt())
                .build();
    }

    private long estimateRowCount(MultipartFile file) {
        try {
            String content = new String(file.getBytes());
            return content.split("\n").length - 1;
        } catch (IOException e) {
            return 0;
        }
    }

    private List<String> extractCsvHeaders(MultipartFile file) {
        try {
            String content = new String(file.getBytes());
            String[] lines = content.split("\n");
            if (lines.length > 0) {
                return List.of(lines[0].split(","));
            }
        } catch (IOException e) {
            log.warn("Could not extract CSV headers");
        }
        return new ArrayList<>();
    }
}
