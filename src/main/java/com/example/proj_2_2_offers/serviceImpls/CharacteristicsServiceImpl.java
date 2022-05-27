package com.example.proj_2_2_offers.serviceImpls;


import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.entities.Characteristic;
import com.example.proj_2_2_offers.entities.CharacteristicNode;
import com.example.proj_2_2_offers.entities.Offer;
import com.example.proj_2_2_offers.repositories.CharacteristicRepo;
import com.example.proj_2_2_offers.repositories.NodeRepo;
import com.example.proj_2_2_offers.repositories.OfferRepo;
import com.example.proj_2_2_offers.services.CharacteristicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class CharacteristicsServiceImpl implements CharacteristicService {
    private OfferRepo offerRepo;
    private CharacteristicRepo characteristicRepo;

    private NodeRepo nodeRepo;

    @Autowired
    public CharacteristicsServiceImpl(OfferRepo offerRepo, CharacteristicRepo characteristicRepo, NodeRepo nodeRepo) {
        this.offerRepo = offerRepo;
        this.characteristicRepo=characteristicRepo;
        this.nodeRepo=nodeRepo;
    }

    public CharacteristicDTO getDTObyObj(CharacteristicNode characteristicNode){
        Characteristic characteristic=characteristicNode.getCharacteristic();
        return CharacteristicDTO.builder()
                .id(characteristicNode.getId())
                .title(characteristic.getTitle())
                .offerId(characteristicNode.getOffer().getId())
                .quantity(characteristicNode.getQuantity())
                .isDurable(characteristicNode.getIsDurable())
                .price(characteristic.getPrice())
                .mainQuantity(characteristicNode.getCharacteristic().getQuantity())
                .build();
    }

    public Characteristic getObjByDTO(CharacteristicDTO characteristicDTO, Offer offer){
        return Characteristic.builder()
                .id(characteristicDTO.getId())
                .title(characteristicDTO.getTitle())
                .price(characteristicDTO.getPrice())
                .quantity(characteristicDTO.getMainQuantity())
                .build();
    }

    public CharacteristicDTO getById(Long id){
        CharacteristicNode characteristic=nodeRepo.findById(id).orElse(null);
        if (characteristic==null) return null;
        return getDTObyObj(characteristic);
    }

    public List<CharacteristicDTO> getAllCharacteristicsByOrderId(Long id){
        Offer offer=offerRepo.findById(id).orElse(null);
        if (offer==null) return null;
        List<CharacteristicDTO> list=nodeRepo.findByOffer(offer).stream().map(this::getDTObyObj).collect(Collectors.toList());
        System.out.println(list);
        return list;
    }

    public void addCharacteristic(CharacteristicDTO characteristicDTO){
        if(!characteristicRepo.existsByTitle(characteristicDTO.getTitle())){
            Characteristic characteristic=Characteristic.builder()
                    .title(characteristicDTO.getTitle())
                    .price(characteristicDTO.getPrice())
                    .build();
            characteristic=characteristicRepo.save(characteristic);
            CharacteristicNode node=CharacteristicNode.builder()
                    .characteristic(characteristic)
                    .offer(offerRepo.getById(characteristicDTO.getOfferId()))
                    .quantity(characteristicDTO.getQuantity())
                    .isDurable(characteristicDTO.getIsDurable())
                    .build();
            nodeRepo.save(node);
        }else {
            CharacteristicNode node=CharacteristicNode.builder()
                    .characteristic(characteristicRepo.getByTitle(characteristicDTO.getTitle()))
                    .offer(offerRepo.getById(characteristicDTO.getOfferId()))
                    .quantity(characteristicDTO.getQuantity())
                    .isDurable(characteristicDTO.getIsDurable())
                    .build();
            nodeRepo.save(node);
        }
        log.info("characteristic {} has been added", characteristicDTO);
    }

    public boolean delete(Long id){
        boolean f=nodeRepo.existsById(id);
        nodeRepo.deleteById(id);
        log.info("characteristic node id={} has been deleted", id);
        return f;
    }

    @Override
    public void update(CharacteristicDTO characteristicDTO) {
        Characteristic characteristic=characteristicRepo.getByTitle(characteristicDTO.getTitle());
        characteristic.setTitle(characteristicDTO.getTitle());
        characteristic.setPrice(characteristicDTO.getPrice());
        characteristic.setQuantity(characteristicDTO.getMainQuantity());
        characteristicRepo.save(characteristic);
        log.info("characteristic {} has been updated", characteristic);
        CharacteristicNode characteristicNode=nodeRepo.getById(characteristicDTO.getId());
        characteristicNode.setQuantity(characteristicDTO.getQuantity());
        characteristicNode.setIsDurable(characteristicDTO.getIsDurable());
        nodeRepo.save(characteristicNode);
    }

    public boolean isExists(Long id){
        return nodeRepo.findById(id).isPresent();
    }

    @Override
    public CharacteristicNode getNodeByDTO(CharacteristicDTO characteristicDTO) {
        return CharacteristicNode.builder()
                .id(characteristicDTO.getId())
                .characteristic(characteristicRepo.getByTitle(characteristicDTO.getTitle()))
                .offer(offerRepo.getById(characteristicDTO.getOfferId()))
                .isDurable(characteristicDTO.getIsDurable())
                .quantity(characteristicDTO.getQuantity())
                .build();
    }

    @Override
    public void addToStorage(Long id, Integer quantity) {
        Characteristic c=characteristicRepo.getById(id);
        c.setQuantity(c.getQuantity());
        characteristicRepo.save(c);
    }

    @Override
    public void getFromStorage(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> cur:map.entrySet()){
            Characteristic c=characteristicRepo.getByTitle(cur.getKey());
            if(c.getQuantity()<cur.getValue()) throw new RuntimeException();
            c.setQuantity(c.getQuantity()-cur.getValue());
            characteristicRepo.save(c);
        }
    }
}
