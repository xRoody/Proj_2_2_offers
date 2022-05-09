package com.example.proj_2_2_offers.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OfferDTO {
    private Long id;
    private String title;
    private Double price;
    private List<CharacteristicDTO> characteristics=new ArrayList<>();
    private Long categoryId;
}
