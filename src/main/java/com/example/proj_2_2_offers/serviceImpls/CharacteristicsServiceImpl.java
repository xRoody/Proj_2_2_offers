package com.example.proj_2_2_offers.serviceImpls;


import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.Offer;
import com.example.proj_2_2_offers.repositories.CharacteristicRepo;
import com.example.proj_2_2_offers.repositories.OfferRepo;
import com.example.proj_2_2_offers.services.CharacteristicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class CharacteristicsServiceImpl implements CharacteristicService {
    private OfferRepo offerRepo;
    private CharacteristicRepo characteristicRepo;

    @Autowired
    public CharacteristicsServiceImpl(OfferRepo offerRepo, CharacteristicRepo characteristicRepo) {
        this.offerRepo = offerRepo;
        this.characteristicRepo=characteristicRepo;
    }

    public CharacteristicDTO getDTObyObj(Characteristic characteristic){
        return CharacteristicDTO.builder()
                .id(characteristic.getId())
                .title(characteristic.getTitle())
                .offerId(characteristic.getOffer().getId())
                .quantity(characteristic.getQuantity())
                .isDurable(characteristic.getIsDurable())
                .price(characteristic.getPrice())
                .build();
    }

    public Characteristic getObjByDTO(CharacteristicDTO characteristicDTO){
        return Characteristic.builder()
                .id(characteristicDTO.getId())
                .title(characteristicDTO.getTitle())
                .offer(offerRepo.getById(characteristicDTO.getOfferId()))
                .isDurable(characteristicDTO.getIsDurable())
                .quantity(characteristicDTO.getQuantity())
                .price(characteristicDTO.getPrice())
                .build();
    }

    public Characteristic getObjByDTO(CharacteristicDTO characteristicDTO, Offer offer){
        return Characteristic.builder()
                .id(characteristicDTO.getId())
                .title(characteristicDTO.getTitle())
                .offer(offer)
                .quantity(characteristicDTO.getQuantity())
                .isDurable(characteristicDTO.getIsDurable())
                .price(characteristicDTO.getPrice())
                .build();
    }

    public CharacteristicDTO getById(Long id){
        Characteristic characteristic=characteristicRepo.findById(id).orElse(null);
        if (characteristic==null) return null;
        return getDTObyObj(characteristic);
    }

    public List<CharacteristicDTO> getAllCharacteristicsByOrderId(Long id){
        Offer offer=offerRepo.findById(id).orElse(null);
        if (offer==null) return null;
        return characteristicRepo.findByOffer(offer).stream().map(this::getDTObyObj).collect(Collectors.toList());
    }

    public void addCharacteristic(CharacteristicDTO characteristicDTO){
        Characteristic characteristic=Characteristic.builder()
                .title(characteristicDTO.getTitle())
                .offer(offerRepo.getById(characteristicDTO.getOfferId()))
                .quantity(characteristicDTO.getQuantity())
                .isDurable(characteristicDTO.getIsDurable())
                .price(characteristicDTO.getPrice())
                .build();
        characteristicRepo.save(characteristic);
        log.info("characteristic {} has been added", characteristicDTO);
    }

    public boolean delete(Long id){
        boolean f=characteristicRepo.existsById(id);
        characteristicRepo.deleteById(id);
        log.info("characteristic id={} has been deleted", id);
        return f;
    }

    @Override
    public void update(CharacteristicDTO characteristicDTO) {
        Characteristic characteristic=characteristicRepo.getById(characteristicDTO.getId());
        characteristic.setTitle(characteristicDTO.getTitle());
        characteristic.setQuantity(characteristicDTO.getQuantity());
        characteristic.setIsDurable(characteristicDTO.getIsDurable());
        characteristic.setPrice(characteristicDTO.getPrice());
        characteristicRepo.save(characteristic);
        log.info("characteristic {} has been updated", characteristicDTO);
    }

    public boolean isExists(Long id){
        return characteristicRepo.findById(id).isPresent();
    }
}
