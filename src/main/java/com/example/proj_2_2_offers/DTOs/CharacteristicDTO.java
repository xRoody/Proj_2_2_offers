package com.example.proj_2_2_offers.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CharacteristicDTO {
    private Long id;
    private String title;
    private Long offerId;
    private Integer quantity;
    private Double price;
    private Boolean isDurable;
    private Integer mainQuantity;
}
