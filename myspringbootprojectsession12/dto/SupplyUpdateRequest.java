package com.example.myspringbootprojectsession12.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SupplyUpdateRequest {
    @NotBlank(message = "Tên vật tư không được để trống")
    private String name;
    private String specification;
    private String provider;
    private final Map<String, Object> unknownFields = new LinkedHashMap<>();

    @JsonAnySetter
    public void addUnknownField(String field, Object value) {
        unknownFields.put(field, value);
    }
}
