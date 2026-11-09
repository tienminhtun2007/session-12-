package com.example.myspringbootprojectsession12.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyExportResponse {
    private Long supplyId;
    private String supplyName;
    private long totalExportQuantity;
}
