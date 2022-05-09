package com.example.proj_2_2_offers.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatPriceWrapper {
    private String catTitle;
    private Double price;
}
