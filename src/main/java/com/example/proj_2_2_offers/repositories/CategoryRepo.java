package com.example.proj_2_2_offers.repositories;

import com.example.proj_2_2_offers.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
