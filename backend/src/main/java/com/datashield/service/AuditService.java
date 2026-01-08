package com.datashield.service;

import com.datashield.domain.AuditLog;
import com.datashield.domain.User;
import com.datashield.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public void logEvent(User user, AuditLog.AuditAction action, String resource, Long resourceId, String details) {
        try {
            String ipAddress = getClientIpAddress();
            AuditLog log = AuditLog.builder()
                    .user(user)
                    .action(action)
                    .resource(resource)
                    .resourceId(resourceId)
                    .details(details)
                    .ipAddress(ipAddress)
                    .build();
            auditLogRepository.save(log);
            log.info("Audit logged: {} - {} - {}", user.getUsername(), action, resource);
        } catch (Exception e) {
            log.error("Error logging audit event", e);
        }
    }

    public void logLoginEvent(User user) {
        logEvent(user, AuditLog.AuditAction.LOGIN, "USER", user.getId(), "User login");
    }

    public void logDatasetUpload(User user, Long datasetId, String datasetName) {
        logEvent(user, AuditLog.AuditAction.DATASET_UPLOADED, "DATASET", datasetId, "Uploaded: " + datasetName);
    }

    public void logPolicyCreated(User user, Long policyId, String policyName) {
        logEvent(user, AuditLog.AuditAction.POLICY_CREATED, "POLICY", policyId, "Created: " + policyName);
    }

    public void logJobStarted(User user, Long jobId) {
        logEvent(user, AuditLog.AuditAction.JOB_STARTED, "JOB", jobId, "Job started");
    }

    public void logJobCompleted(User user, Long jobId) {
        logEvent(user, AuditLog.AuditAction.JOB_COMPLETED, "JOB", jobId, "Job completed");
    }

    private String getClientIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "UNKNOWN";
        }
        HttpServletRequest request = attributes.getRequest();
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
