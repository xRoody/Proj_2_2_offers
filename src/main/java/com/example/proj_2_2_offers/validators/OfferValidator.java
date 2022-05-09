package com.example.proj_2_2_offers.validators;


import com.example.proj_2_2_offers.DTOs.OfferDTO;
import com.example.proj_2_2_offers.exceptions.BodyExceptionWrapper;
import com.example.proj_2_2_offers.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class OfferValidator {
    private static final Pattern pattern = Pattern.compile("[A-Za-z\\s]*");
    private CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<BodyExceptionWrapper> validateNewOffer(OfferDTO offerDTO) {
        List<BodyExceptionWrapper> reports = new ArrayList<>();
        if (offerDTO.getPrice() <= 0) reports.add(new BodyExceptionWrapper("e-002", "Price must be higher than 0"));
        if (offerDTO.getTitle() == null || offerDTO.getTitle().isBlank())
            reports.add(new BodyExceptionWrapper("e-001", "Title must be not empty"));
        else if (!pattern.matcher(offerDTO.getTitle()).matches())
            reports.add(new BodyExceptionWrapper("e-002", "Incorrect format of title"));
        if (offerDTO.getCategoryId() == null)
            reports.add(new BodyExceptionWrapper("e-001", "Category must be not null"));
        else if (!categoryService.isExists(offerDTO.getCategoryId()))
            reports.add(new BodyExceptionWrapper("e-003", "This category is not exists"));
        return reports;
    }

    public List<BodyExceptionWrapper> validateExistsOffer(OfferDTO offerDTO) {
        List<BodyExceptionWrapper> reports = new ArrayList<>(validateNewOffer(offerDTO));
        for (int i = 0; i < offerDTO.getCharacteristics().size(); i++) {
            if (offerDTO.getCharacteristics().get(i).getId() != null && !Objects.equals(offerDTO.getCharacteristics().get(i).getOfferId(), offerDTO.getId()))
                reports.add(new BodyExceptionWrapper("e-002", "Incorrect ratio of ids (characteristic[" + i + "] is exists, but for other offer)"));
        }
        return reports;
    }
}
