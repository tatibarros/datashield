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
}
