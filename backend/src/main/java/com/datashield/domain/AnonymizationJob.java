package com.datashield.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "anonymization_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymizationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private AnonymizationPolicy policy;

    @ManyToOne
    @JoinColumn(name = "triggered_by", nullable = false)
    private User triggeredBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "input_file_path", nullable = false)
    private String inputFilePath;

    @Column(name = "output_file_path")
    private String outputFilePath;

    @Column(name = "processed_rows")
    private Long processedRows;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum JobStatus {
        QUEUED, RUNNING, SUCCEEDED, FAILED
    }
}
