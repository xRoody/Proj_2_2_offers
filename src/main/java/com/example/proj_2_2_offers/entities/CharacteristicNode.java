package com.example.proj_2_2_offers.entities;

import javax.persistence.Column;

public class CharacteristicNode {

    @Column(name = "char_id")
    private Long charId;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "durable")
    private Boolean isDurable;}
