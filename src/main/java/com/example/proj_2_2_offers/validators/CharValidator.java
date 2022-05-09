package com.example.proj_2_2_offers.validators;


import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.exceptions.BodyExceptionWrapper;
import com.example.proj_2_2_offers.services.CharacteristicService;
import com.example.proj_2_2_offers.services.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.ArrayList;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CharValidator {
    private final OfferService offerService;
    private final CharacteristicService characteristicService;
    private final static Pattern titlePattern=Pattern.compile("[A-Z][A-Za-z]*");

    public List<BodyExceptionWrapper> validate(CharacteristicDTO characteristicDTO){
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        validateTitle(characteristicDTO.getTitle(), reports);
        validateOffer(characteristicDTO.getOfferId(), reports);
        validatePrice(characteristicDTO.getPrice(),  reports);
        validateQuantity(characteristicDTO.getQuantity(), reports);
        return reports;
    }

    private void validateTitle(String title, List<BodyExceptionWrapper> reports){
        if (title==null || title.isBlank()) reports.add(new BodyExceptionWrapper("e-001", "Title must be not empty"));
        else if (!titlePattern.matcher(title).matches()) reports.add(new BodyExceptionWrapper("e-002", "Incorrect title format"));
    }


    private void validateOffer(Long offerId, List<BodyExceptionWrapper> reports){
        if (!offerService.isExists(offerId)) reports.add(new BodyExceptionWrapper("e-003", "Offer with id="+offerId+" is not exists"));
    }

    public List<BodyExceptionWrapper> validateUpdate(CharacteristicDTO characteristicDTO){
        List<BodyExceptionWrapper> reports=new ArrayList<>();
        if (!characteristicService.isExists(characteristicDTO.getId())) reports.add(new BodyExceptionWrapper("e-003", "Characteristic with id="+characteristicDTO.getId()+" is not exists"));
        else reports.addAll(validate(characteristicDTO));
        return reports;
    }

    private void validatePrice(Number num,  List<BodyExceptionWrapper> reports){
        if(num==null) reports.add(new BodyExceptionWrapper("e-001", "price must be not null"));
        else if (num.doubleValue()<=0) reports.add(new BodyExceptionWrapper("e-002", "price must be above zero"));
    }

    private void validateQuantity(Number num,  List<BodyExceptionWrapper> reports){
        if(num==null) reports.add(new BodyExceptionWrapper("e-001", "quantity must be not null"));
        else if (num.doubleValue()<0) reports.add(new BodyExceptionWrapper("e-002", "quantity must be above or equals zero"));
    }
}
