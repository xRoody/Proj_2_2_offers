package com.example.proj_2_2_offers.services;



import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.CharacteristicNode;
import com.example.proj_2_2_offers.entities.Offer;

import java.util.List;
import java.util.Map;

public interface CharacteristicService {
    CharacteristicDTO getDTObyObj(CharacteristicNode characteristic);
    void update(CharacteristicDTO characteristicDTO);
    boolean delete(Long id);
    void addCharacteristic(CharacteristicDTO characteristicDTO);
    List<CharacteristicDTO> getAllCharacteristicsByOrderId(Long id);
    CharacteristicDTO getById(Long id);
    boolean isExists(Long id);

    CharacteristicNode getNodeByDTO(CharacteristicDTO characteristicDTO);

    void addToStorage(Long id, Integer quantity);

    void getFromStorage(Map<String, Integer> map);
}
