package com.apirest.apirest.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "seat")
@Data
public class Seat {
    @Id
    @Column(name = "id_seat", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSeat;
    @Column(name = "[row]")
    private int row;
    @Column(name = "[column]")
    private String column;
    @Column(name = "[type]")
    private String type;
    @Column(name = "[status]")
    private String status; //available, ocuppied

    @ManyToOne
    @JoinColumn(name = "id_flight")
    private Flight flight;

    @Column(name = "emergency_near")
    private Boolean emergencyNear;




}
