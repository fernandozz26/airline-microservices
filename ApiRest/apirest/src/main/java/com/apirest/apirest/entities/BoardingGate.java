package com.apirest.apirest.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "boarding_gate")
public class BoardingGate {
    @Id
    @Column(name = "id_boarding_gate", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBoardingGate;

    private String gate;

}
