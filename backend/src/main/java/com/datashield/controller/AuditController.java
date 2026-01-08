package com.datashield.controller;

import com.datashield.domain.AuditLog;
import com.datashield.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit", description = "Audit log endpoints")
public class AuditController {
    
    private final AuditLogRepository auditLogRepository;

    @GetMapping
    @Operation(summary = "Get recent audit logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs(@RequestParam(defaultValue = "100") int limit) {
        List<AuditLog> logs = auditLogRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .toList();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit logs for a specific user")
    public ResponseEntity<List<AuditLog>> getUserAuditLogs(@PathVariable Long userId) {
        List<AuditLog> logs = auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Get audit logs by action")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(@PathVariable String action) {
        AuditLog.AuditAction auditAction = AuditLog.AuditAction.valueOf(action);
        List<AuditLog> logs = auditLogRepository.findByActionOrderByCreatedAtDesc(auditAction);
        return ResponseEntity.ok(logs);
    }
}
