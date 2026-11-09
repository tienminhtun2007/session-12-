package com.example.myspringbootprojectsession12.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopExportResponse {
    private String topSupplyName;
    private long totalExportQuantity;
}
