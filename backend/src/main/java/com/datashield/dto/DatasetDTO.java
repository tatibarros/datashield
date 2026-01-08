package com.datashield.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatasetDTO {
    private Long id;
    private String name;
    private String fileType;
    private Long rowCount;
    private Integer columnCount;
    private List<String> columns;
    private String ownerUsername;
    private LocalDateTime createdAt;
}
