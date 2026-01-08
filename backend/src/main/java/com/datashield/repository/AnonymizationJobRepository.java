package com.datashield.repository;

import com.datashield.domain.AnonymizationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnonymizationJobRepository extends JpaRepository<AnonymizationJob, Long> {
    List<AnonymizationJob> findByStatusOrderByCreatedAtDesc(AnonymizationJob.JobStatus status);
    List<AnonymizationJob> findByDatasetIdOrderByCreatedAtDesc(Long datasetId);
}
