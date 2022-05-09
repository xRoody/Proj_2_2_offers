package com.example.proj_2_2_offers.repositories;

import com.example.proj_2_2_offers.entities.Category;
import com.example.proj_2_2_offers.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepo extends JpaRepository<Offer,Long> {
    long countByCategory(Category category);
    List<Offer> findAllByCategory(Category category);
}
