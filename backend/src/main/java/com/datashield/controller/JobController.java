package com.datashield.controller;

import com.datashield.domain.AnonymizationJob;
import com.datashield.repository.AnonymizationJobRepository;
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
}
