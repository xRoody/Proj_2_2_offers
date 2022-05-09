package com.example.proj_2_2_offers.controllers;


import com.example.proj_2_2_offers.DTOs.CharacteristicDTO;
import com.example.proj_2_2_offers.exceptions.BodyExceptionWrapper;
import com.example.proj_2_2_offers.services.CharacteristicService;
import com.example.proj_2_2_offers.validators.CharValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

import java.net.URI;
import java.util.List;


@Slf4j
@RequestMapping("/characteristics")
@RestController
@AllArgsConstructor
public class CharacteristicController {
    private CharacteristicService characteristicService;
    private CharValidator validator;


    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        CharacteristicDTO dto =characteristicService.getById(id);
        if (dto==null) {
            log.info("No characteristic with id={} found", id);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addNewChar(@RequestBody CharacteristicDTO characteristicDTO){
        List<BodyExceptionWrapper> reports=validator.validate(characteristicDTO);
        if (reports.size()!=0) {
            log.info(reports.toString());
            return new ResponseEntity<>(reports, HttpStatus.BAD_REQUEST);
        }
        characteristicService.addCharacteristic(characteristicDTO);
        return ResponseEntity.created(URI.create("/characteristics")).build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody CharacteristicDTO characteristicDTO){
        List<BodyExceptionWrapper> reports=validator.validateUpdate(characteristicDTO);
        if (reports.size()!=0) {
            log.info("No category with id={} has been updated (conflict)", characteristicDTO.getId());
            return new ResponseEntity<>(reports,HttpStatus.CONFLICT);
        }
        characteristicService.update(characteristicDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteChar(@PathVariable("id") Long id){
        if(characteristicService.delete(id)) return ResponseEntity.ok().build();
        log.info("No category with id={} deleted", id);
        return ResponseEntity.noContent().build();
    }
}
