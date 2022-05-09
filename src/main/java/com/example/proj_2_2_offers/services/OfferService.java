package com.example.proj_2_2_offers.services;



import com.example.proj_2_2_offers.DTOs.OfferDTO;
import com.example.proj_2_2_offers.entities.Offer;

import java.util.List;

public interface OfferService {
    OfferDTO getDTOById(Long id);
    List<OfferDTO> getAllOffers();
    boolean deleteOffer(Long id);
    void createOffer(OfferDTO offerDTO);
    void updateOffer(OfferDTO offerDTO);
    OfferDTO getDTOByObj(Offer offer);
    boolean isExists(Long id);

    Double getPrice(OfferDTO offer);

    List<OfferDTO> getAllOffersByCategory(Long id);
}
