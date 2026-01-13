package com.datashield.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class PolicyRequest {
    private Long datasetId;
    private String name;
    private JsonNode rules;
}
