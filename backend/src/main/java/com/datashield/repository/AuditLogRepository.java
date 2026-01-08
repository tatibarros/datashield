package com.datashield.repository;

import com.datashield.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<AuditLog> findByActionOrderByCreatedAtDesc(AuditLog.AuditAction action);
    List<AuditLog> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);
}
