package com.example.proj_2_2_offers.serviceImpls;


import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.DTOs.OfferDTO;
import com.example.proj_2_2_offers.entities.Category;
import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.CharacteristicNode;
import com.example.proj_2_2_offers.entities.Offer;
import com.example.proj_2_2_offers.repositories.CategoryRepo;
import com.example.proj_2_2_offers.repositories.OfferRepo;
import com.example.proj_2_2_offers.services.CharacteristicService;
import com.example.proj_2_2_offers.services.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class OfferServiceImpl implements OfferService {
    private OfferRepo offerRepo;
    private CategoryRepo categoryRepo;
    private CharacteristicService characteristicService;

    @Autowired
    public OfferServiceImpl(OfferRepo offerRepo, CharacteristicService characteristicService, CategoryRepo categoryRepo) {
        this.offerRepo = offerRepo;
        this.characteristicService=characteristicService;
        this.categoryRepo=categoryRepo;
    }

    public OfferDTO getDTOByObj(Offer offer){
        boolean f=false;
        List<CharacteristicDTO> list=new ArrayList<>();
        OfferDTO offerDTO= OfferDTO.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .price(offer.getPrice())
                .categoryId(offer.getCategory().getId())
                .build();
        for (CharacteristicNode c:offer.getCharacteristics()){
            if (c.getCharacteristic().getQuantity()==null || c.getCharacteristic().getQuantity()<=0) f=true;
            list.add(characteristicService.getDTObyObj(c));
        }
        offerDTO.setCharacteristics(list);
        offerDTO.setAvailable(f);
        return offerDTO;
    }

    public OfferDTO getDTOById(Long id){
        Offer offer=offerRepo.findById(id).orElse(null);
        if (offer==null) return null;
        return getDTOByObj(offer);
    }

    public List<OfferDTO> getAllOffers(){
       return offerRepo.findAll().stream().map(x->getDTOByObj(x)).collect(Collectors.toList());
    }

    public boolean deleteOffer(Long id){
        Offer offer=offerRepo.findById(id).orElse(null);
        if (offer!=null) {
            Category category=offer.getCategory();
            offerRepo.deleteById(id);
            log.info("offer {} has been deleted", offer);
            if (offerRepo.countByCategory(category)== 0) {
                log.info("category title={} deleted, cause empty", category.getTitle());
                categoryRepo.deleteById(category.getId());
            }
            return true;
        }
        return false;
    }

    public void createOffer(OfferDTO offerDTO){
        Offer offer= Offer.builder()
                .title(offerDTO.getTitle())
                .price(offerDTO.getPrice())
                .category(categoryRepo.getById(offerDTO.getCategoryId()))
                .characteristics(new HashSet<>())
                .build();
        offerRepo.save(offer);
        for (CharacteristicDTO dto:offerDTO.getCharacteristics()){
            dto.setOfferId(offer.getId());
            dto.setId(null);
            characteristicService.addCharacteristic(dto);
        }
        log.info("offer {} has been added", offerDTO);
    }

    public void updateOffer(OfferDTO offerDTO){
        if (offerDTO.getId()==null) throw new IllegalStateException("id must be not null");
        Offer offer=offerRepo.getById(offerDTO.getId());
        offer.setPrice(offerDTO.getPrice());
        offer.setTitle(offerDTO.getTitle());
        offer.setCategory(categoryRepo.getById(offerDTO.getCategoryId()));
        for (CharacteristicDTO characteristicDTO:offerDTO.getCharacteristics()){
            if (characteristicDTO.getId()!=null){
                characteristicService.update(characteristicDTO);
            }else {
                characteristicDTO.setOfferId(offer.getId());
                offer.getCharacteristics().add(characteristicService.getNodeByDTO(characteristicDTO));
            }
        }
        offerRepo.save(offer);
        log.info("offer {} has been updated", offerDTO);
    }

    @Override
    public Double getPrice(OfferDTO offer) {
        return offer.getPrice()+offer.getCharacteristics().stream().mapToDouble(x->x.getPrice()* x.getQuantity()).sum();
    }

    public boolean isExists(Long id){
        return offerRepo.findById(id).isPresent();
    }

    @Override
    public List<OfferDTO> getAllOffersByCategory(Long id) {
        Category category=categoryRepo.findById(id).orElse(null);
        if (category==null) return null;
        List<OfferDTO> offers=offerRepo.findAllByCategory(category).stream().map(x->getDTOByObj(x)).collect(Collectors.toList());
        return offers;
    }
}
