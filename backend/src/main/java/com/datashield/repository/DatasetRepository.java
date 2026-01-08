package com.datashield.repository;

import com.datashield.domain.Dataset;
import com.datashield.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findByOwner(User owner);
    List<Dataset> findByOwnerOrderByCreatedAtDesc(User owner);
}
