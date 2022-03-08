package com.apirest.apirest.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "boarding_pass")
@Data
public class BoardingPass {
    @Id
    @Column(name = "id_boarding_pass", nullable = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBoardingPass;
    @NotEmpty
    @JoinColumn(name = "id_seat")
    @OneToOne
    private Seat seat;

    @JoinColumn(name = "id_reservation")
    @OneToOne
    private Reservation reservation;


}
