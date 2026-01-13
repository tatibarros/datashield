package com.datashield.controller;

import com.datashield.domain.AnonymizationPolicy;
import com.datashield.repository.AnonymizationPolicyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.datashield.domain.Dataset;
import com.datashield.domain.User;
import com.datashield.repository.DatasetRepository;
import com.datashield.repository.UserRepository;
import com.datashield.service.AuditService;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Tag(name = "Policies", description = "Anonymization policy endpoints")
public class PolicyController {
    
    private final AnonymizationPolicyRepository policyRepository;
    private final DatasetRepository datasetRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @GetMapping("/dataset/{datasetId}")
    @Operation(summary = "Get policies for a dataset")
    public ResponseEntity<List<AnonymizationPolicy>> getPoliciesForDataset(@PathVariable Long datasetId) {
        List<AnonymizationPolicy> policies = policyRepository.findByDatasetIdOrderByCreatedAtDesc(datasetId);
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get policy details")
    public ResponseEntity<AnonymizationPolicy> getPolicy(@PathVariable Long id) {
        AnonymizationPolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        return ResponseEntity.ok(policy);
    }

    @PostMapping
    @Operation(summary = "Create a new anonymization policy")
    public ResponseEntity<AnonymizationPolicy> createPolicy(@RequestBody com.datashield.dto.PolicyRequest request) {
        Dataset dataset = datasetRepository.findById(request.getDatasetId())
                .orElseThrow(() -> new RuntimeException("Dataset not found"));

        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null) {
            String username = authentication.getName();
            user = userRepository.findByUsername(username).orElse(null);
        }
        if (user == null) {
            user = dataset.getOwner();
        }

        AnonymizationPolicy policy = AnonymizationPolicy.builder()
                .name(request.getName())
                .dataset(dataset)
                .createdBy(user)
                .rules(request.getRules())
                .active(true)
                .build();

        AnonymizationPolicy saved = policyRepository.save(policy);
        auditService.logPolicyCreated(user, saved.getId(), saved.getName());

        return ResponseEntity.ok(saved);
    }
}
