package com.example.proj_2_2_offers.controllers;


import com.example.proj_2_2_offers.DTOs.CatPriceWrapper;
import com.example.proj_2_2_offers.DTOs.OfferDTO;
import com.example.proj_2_2_offers.exceptions.BodyExceptionWrapper;
import com.example.proj_2_2_offers.serviceImpls.OfferServiceImpl;
import com.example.proj_2_2_offers.services.CategoryService;
import com.example.proj_2_2_offers.services.CharacteristicService;
import com.example.proj_2_2_offers.validators.OfferValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


import java.net.URI;
import java.util.List;


@Slf4j
@RequestMapping("/offers")
@RestController
@RequiredArgsConstructor
public class OffersController {
    private final OfferServiceImpl offerService;
    private final OfferValidator offerValidator;
    private final CharacteristicService characteristicService;
    private final CategoryService categoryService;
    private final WebClient client=WebClient.create("http://localhost:8082");

    @GetMapping
    public List<OfferDTO> getAll() {
        return offerService.getAllOffers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        OfferDTO offerDTO = offerService.getDTOById(id);
        if (offerDTO == null) {
            log.info("No offers with id={} found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offerDTO);
    }

    @PostMapping
    public ResponseEntity<Object> addNewOffer(@RequestBody OfferDTO offerDTO) {
        List<BodyExceptionWrapper> reports = offerValidator.validateNewOffer(offerDTO);
        if (reports.size() != 0) {
            log.info(reports.toString());
            return new ResponseEntity<>(reports , HttpStatus.BAD_REQUEST);
        }
        offerService.createOffer(offerDTO);
        return ResponseEntity.created(URI.create("/offers")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        Long count=client.get().uri("/card/countByOfferId/{id}", id).retrieve().toEntity(Long.class).block().getBody();
        if (count!=0){
            return new ResponseEntity<>(new BodyExceptionWrapper("Not deleted", "This offer has attachments with order card"), HttpStatus.BAD_REQUEST);
        }
        boolean f = offerService.deleteOffer(id);
        if (f) return ResponseEntity.ok().build();
        log.info("No category with id={} deleted", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Object> updateOffer(@RequestBody OfferDTO offerDTO) {
        List<BodyExceptionWrapper> reports = offerValidator.validateExistsOffer(offerDTO);
        if (reports.size() != 0) {
            log.info("No category with id={} updated (conflict)", offerDTO.getId());
            return new ResponseEntity<>(reports , HttpStatus.CONFLICT);
        }
        offerService.updateOffer(offerDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/characteristics")
    public ResponseEntity<Object> getCharsByOfferId(@PathVariable("id") Long id) {
        if (!offerService.isExists(id)) {
            log.info("No offer with id={} found", id);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(characteristicService.getAllCharacteristicsByOrderId(id) ,HttpStatus.OK);
    }

    @GetMapping("/isExists/{id}")
    public ResponseEntity<Object> isOfferExists(@PathVariable("id") Long id) {
        if (offerService.isExists(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/categoryAndPrice")
    public ResponseEntity<Object> getCategoryAndPrice(@PathVariable("id") Long id) {
        OfferDTO offerDTO=offerService.getDTOById(id);
        if (offerDTO==null) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(new CatPriceWrapper(categoryService.getById(offerDTO.getCategoryId()).getTitle(), offerService.getPrice(offerDTO)), HttpStatus.OK);
    }
}