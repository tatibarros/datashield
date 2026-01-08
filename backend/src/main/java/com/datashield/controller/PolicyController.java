package com.datashield.controller;

import com.datashield.domain.AnonymizationPolicy;
import com.datashield.repository.AnonymizationPolicyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Tag(name = "Policies", description = "Anonymization policy endpoints")
public class PolicyController {
    
    private final AnonymizationPolicyRepository policyRepository;

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
}
