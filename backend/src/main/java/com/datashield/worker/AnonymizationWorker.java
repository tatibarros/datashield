package com.datashield.service;

import com.datashield.domain.AnonymizationJob;
import com.datashield.domain.Dataset;
import com.datashield.repository.AnonymizationJobRepository;
import com.datashield.repository.DatasetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnonymizationWorker {
    private final AnonymizationJobRepository jobRepository;
    private final DatasetRepository datasetRepository;
    private final com.datashield.service.AuditService auditService;

    @Scheduled(fixedDelay = 5000)
    public void processJobs() {
        List<AnonymizationJob> queuedJobs = jobRepository.findByStatusOrderByCreatedAtDesc(AnonymizationJob.JobStatus.QUEUED);
        
        for (AnonymizationJob job : queuedJobs) {
            processJob(job);
        }
    }

    private void processJob(AnonymizationJob job) {
        try {
            log.info("Processing job {} for dataset {}", job.getId(), job.getDataset().getId());
            
            job.setStatus(AnonymizationJob.JobStatus.RUNNING);
            job.setStartedAt(LocalDateTime.now());
            jobRepository.save(job);

            // Simulate processing
            Dataset dataset = job.getDataset();
            long processedRows = dataset.getRowCount();
            
            Thread.sleep(2000);

            job.setProcessedRows(processedRows);
            job.setStatus(AnonymizationJob.JobStatus.SUCCEEDED);
            job.setCompletedAt(LocalDateTime.now());
            job.setOutputFilePath(job.getInputFilePath() + ".anonymized");
            jobRepository.save(job);

            log.info("Job {} completed successfully. Processed {} rows", job.getId(), processedRows);
            // audit log
            if (job.getTriggeredBy() != null) {
                auditService.logJobCompleted(job.getTriggeredBy(), job.getId());
            }
        } catch (Exception e) {
            log.error("Error processing job {}: {}", job.getId(), e.getMessage(), e);
            job.setStatus(AnonymizationJob.JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);
            // audit failure
            if (job.getTriggeredBy() != null) {
                auditService.logEvent(job.getTriggeredBy(), com.datashield.domain.AuditLog.AuditAction.JOB_FAILED, "JOB", job.getId(), "Job failed: " + e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupOldData() {
        log.info("Running cleanup task for old anonymization results");
        // Implementation for data retention policy
    }
}
