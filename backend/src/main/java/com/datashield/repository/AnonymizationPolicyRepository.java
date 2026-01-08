package com.datashield.repository;

import com.datashield.domain.AnonymizationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnonymizationPolicyRepository extends JpaRepository<AnonymizationPolicy, Long> {
    List<AnonymizationPolicy> findByDatasetIdOrderByCreatedAtDesc(Long datasetId);
    List<AnonymizationPolicy> findByDatasetIdAndActive(Long datasetId, Boolean active);
}
