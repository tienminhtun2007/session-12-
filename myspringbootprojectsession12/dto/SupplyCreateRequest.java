package com.example.myspringbootprojectsession12.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplyCreateRequest {
    @NotBlank(message = "Tên vật tư không được để trống")
    private String name;
    private String specification;
    private String provider;
}
