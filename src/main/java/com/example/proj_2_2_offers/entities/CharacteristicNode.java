package com.example.proj_2_2_offers.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "char_node")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacteristicNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;
    @ManyToOne
    @JoinColumn(name = "char_id")
    private Characteristic characteristic;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "durable")
    private Boolean isDurable;}
