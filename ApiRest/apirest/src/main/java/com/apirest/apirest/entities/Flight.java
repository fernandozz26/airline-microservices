package com.apirest.apirest.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "flight")
public class Flight {
    @Id
    @Column(name = "id_flight", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFlight;

    private String origin;
    private String destiny;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "departure_date")
    private Date departureDate;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "arrival_date")
    private Date arrivalDate;

    @ManyToOne
    @JoinColumn(name = "id_boarding_gate")
    private BoardingGate boardingGate;


}
