package com.example.proj_2_2_offers.services;



import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.Offer;

import java.util.List;

public interface CharacteristicService {
    CharacteristicDTO getDTObyObj(Characteristic characteristic);
    Characteristic getObjByDTO(CharacteristicDTO characteristicDTO);
    Characteristic getObjByDTO(CharacteristicDTO characteristicDTO, Offer offer);
    void update(CharacteristicDTO characteristicDTO);
    boolean delete(Long id);
    void addCharacteristic(CharacteristicDTO characteristicDTO);
    List<CharacteristicDTO> getAllCharacteristicsByOrderId(Long id);
    CharacteristicDTO getById(Long id);
    boolean isExists(Long id);
}
