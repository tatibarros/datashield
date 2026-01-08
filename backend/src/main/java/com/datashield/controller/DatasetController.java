package com.datashield.controller;

import com.datashield.domain.Dataset;
import com.datashield.domain.User;
import com.datashield.dto.DatasetDTO;
import com.datashield.repository.UserRepository;
import com.datashield.service.DatasetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
@RequiredArgsConstructor
@Tag(name = "Datasets", description = "Dataset management endpoints")
@SecurityRequirement(name = "bearer-jwt")
public class DatasetController {
    
    private final DatasetService datasetService;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    @Operation(summary = "Upload a CSV or JSON dataset")
    public ResponseEntity<DatasetDTO> uploadDataset(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description) {
        
        User currentUser = userRepository.findByUsername("admin").orElseThrow();
        DatasetDTO dataset = datasetService.uploadDataset(file, name, currentUser);
        return ResponseEntity.ok(dataset);
    }

    @GetMapping
    @Operation(summary = "List all datasets for current user")
    public ResponseEntity<List<DatasetDTO>> listDatasets() {
        User currentUser = userRepository.findByUsername("admin").orElseThrow();
        List<DatasetDTO> datasets = datasetService.listDatasets(currentUser);
        return ResponseEntity.ok(datasets);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dataset details")
    public ResponseEntity<DatasetDTO> getDataset(@PathVariable Long id) {
        DatasetDTO dataset = datasetService.getDataset(id);
        return ResponseEntity.ok(dataset);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dataset")
    public ResponseEntity<Void> deleteDataset(@PathVariable Long id) {
        datasetService.deleteDataset(id);
        return ResponseEntity.noContent().build();
    }
}
