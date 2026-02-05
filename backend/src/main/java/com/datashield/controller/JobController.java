package com.datashield.controller;

import com.datashield.domain.AnonymizationJob;
import com.datashield.domain.Dataset;
import com.datashield.domain.AnonymizationPolicy;
import com.datashield.domain.User;
import com.datashield.repository.AnonymizationJobRepository;
import com.datashield.repository.DatasetRepository;
import com.datashield.repository.AnonymizationPolicyRepository;
import com.datashield.repository.UserRepository;
import com.datashield.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Anonymization job endpoints")
public class JobController {
    
    private final AnonymizationJobRepository jobRepository;
    private final DatasetRepository datasetRepository;
    private final AnonymizationPolicyRepository policyRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @GetMapping("/dataset/{datasetId}")
    @Operation(summary = "Get jobs for a dataset")
    public ResponseEntity<List<AnonymizationJob>> getJobsForDataset(@PathVariable Long datasetId) {
        List<AnonymizationJob> jobs = jobRepository.findByDatasetIdOrderByCreatedAtDesc(datasetId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job details")
    public ResponseEntity<AnonymizationJob> getJob(@PathVariable Long id) {
        AnonymizationJob job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return ResponseEntity.ok(job);
    }

    @GetMapping("/status/queued")
    @Operation(summary = "Get queued jobs")
    public ResponseEntity<List<AnonymizationJob>> getQueuedJobs() {
        List<AnonymizationJob> jobs = jobRepository.findByStatusOrderByCreatedAtDesc(AnonymizationJob.JobStatus.QUEUED);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping(value = {"","/start"})
    @Operation(summary = "Start an anonymization job (enqueue)")
    public ResponseEntity<AnonymizationJob> startJob(@RequestParam Long datasetId, @RequestParam Long policyId) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        if (username == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Dataset dataset = datasetRepository.findById(datasetId).orElseThrow(() -> new RuntimeException("Dataset not found"));
        AnonymizationPolicy policy = policyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));

        AnonymizationJob job = AnonymizationJob.builder()
                .dataset(dataset)
                .policy(policy)
                .triggeredBy(user)
                .status(AnonymizationJob.JobStatus.QUEUED)
                .inputFilePath(dataset.getFilePath())
                .build();

        AnonymizationJob saved = jobRepository.save(job);
        // audit: log queued event
        auditService.logJobStarted(user, saved.getId());

        return ResponseEntity.ok(saved);
    }

        @GetMapping("/{id}/download")
        @Operation(summary = "Download anonymized output for a job")
        public ResponseEntity<Resource> downloadJobOutput(@PathVariable Long id) {
            AnonymizationJob job = jobRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
            if (job.getOutputFilePath() == null) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Output not available");
            }
            try {
                Path file = Paths.get(job.getOutputFilePath());
                Resource resource = new UrlResource(file.toUri());
                if (!resource.exists()) {
                    throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "File not found");
                }
                String fileName = file.getFileName().toString();
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            } catch (Exception e) {
                throw new RuntimeException("Could not serve file", e);
            }
        }
}
