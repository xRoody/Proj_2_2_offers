package com.example.proj_2_2_offers.controllers;


import com.example.proj_2_2_offers.DTOs.CategoryDTO;
import com.example.proj_2_2_offers.DTOs.OfferDTO;
import com.example.proj_2_2_offers.services.CategoryService;
import com.example.proj_2_2_offers.services.OfferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.websocket.server.PathParam;

import java.net.URI;
import java.util.List;

@Slf4j
@RequestMapping("/category")
@RestController
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;
    private OfferService offerService;

    @GetMapping
    public List<CategoryDTO> getAll(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id){
        CategoryDTO categoryDTO=categoryService.getById(id);
        if (categoryDTO==null) {
            log.info("No category with id={} found", id);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody CategoryDTO categoryDTO){
        System.out.println(categoryDTO);
        categoryService.add(categoryDTO);
        return ResponseEntity.created(URI.create("/category")).build();
    }

    @PutMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Object>  update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/allOffers")
    public ResponseEntity<Object> getAllOffersFrom(@PathVariable("id") Long id){
        List<OfferDTO> dtos=offerService.getAllOffersByCategory(id);
        if (dtos==null) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        if(categoryService.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }
}
