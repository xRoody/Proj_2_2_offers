package com.example.proj_2_2_offers.serviceImpls;


import com.example.proj_2_2_offers.DTOs.CategoryDTO;
import com.example.proj_2_2_offers.DTOs.OfferDTO;
import com.example.proj_2_2_offers.entities.Category;
import com.example.proj_2_2_offers.repositories.CategoryRepo;
import com.example.proj_2_2_offers.services.CategoryService;
import com.example.proj_2_2_offers.services.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepo categoryRepo;
    private OfferService offerService;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo, OfferService offerService) {
        this.categoryRepo = categoryRepo;
        this.offerService=offerService;
    }

    @Override
    public boolean isExists(Long categoryId) {
        return categoryRepo.existsById(categoryId);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream().map(this::getDTOByObj).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getById(Long id) {
        return getDTOByObj(categoryRepo.findById(id).orElse(null));
    }

    @Override
    public void add(CategoryDTO categoryDTO) {
        Category category=Category.builder()
                .title(categoryDTO.getTitle())
                .build();
        categoryRepo.save(category);
        log.info("Category {} has been added", categoryDTO);
    }

    public void update(CategoryDTO categoryDTO){
        Category category=categoryRepo.getById(categoryDTO.getId());
        category.setTitle(categoryDTO.getTitle());
        category.getOffers().clear();
        for (OfferDTO dto:categoryDTO.getOffers()){
            dto.setCategoryId(category.getId());
            if (offerService.isExists(dto.getId())){
                offerService.updateOffer(dto);
            }else {
                dto.setId(null);
                offerService.createOffer(dto);
            }
        }
        categoryRepo.save(category);
        log.info("category id={} has been updated", categoryDTO.getId());
    }

    @Override
    public boolean delete(Long id) {
        if(isExists(id)){
            categoryRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private CategoryDTO getDTOByObj(Category category){
        if (category==null) return null;
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitle())
                .offers(category.getOffers().stream().map(x->offerService.getDTOByObj(x)).collect(Collectors.toList()))
                .build();
    }



}
