package com.example.proj_2_2_offers.repositories;

import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.CharacteristicNode;
import com.example.proj_2_2_offers.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeRepo extends JpaRepository<CharacteristicNode,Long> {
    List<CharacteristicNode> findByOffer(Offer offer);
}
