package com.example.myspringbootprojectsession12.dto;

import com.example.myspringbootprojectsession12.entities.Supply;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SupplyResponse {
    private Long id;
    private String name;
    private String specification;
    private String provider;
    private int quantity;
    private boolean isDeleted;

    public static SupplyResponse from(Supply supply) {
        return new SupplyResponse(supply.getId(), supply.getName(), supply.getSpecification(),
                supply.getProvider(), supply.getQuantity(), supply.isDeleted());
    }
}
