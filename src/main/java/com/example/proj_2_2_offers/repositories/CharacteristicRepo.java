package com.example.proj_2_2_offers.repositories;

import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CharacteristicRepo extends JpaRepository<Characteristic, Long> {
    List<Characteristic> findByOffer(Offer offer);
}
