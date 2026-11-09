package com.example.myspringbootprojectsession12.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockAmountRequest {
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int amount;
}
