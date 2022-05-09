package com.example.proj_2_2_offers.services;



import com.example.proj_2_2_offers.DTOs.CategoryDTO;

import java.util.List;

public interface CategoryService {
    boolean isExists(Long categoryId);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getById(Long id);
    void add(CategoryDTO categoryDTO);
    void update(CategoryDTO categoryDTO);

    boolean delete(Long id);
}
