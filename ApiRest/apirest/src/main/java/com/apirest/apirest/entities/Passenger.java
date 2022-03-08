package com.apirest.apirest.entities;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "passenger")
@Data
public class Passenger implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_passenger", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPassenger;
    @Column
    private String passport;
    @Column
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String email;
    @Temporal(value = TemporalType.DATE)
    private Date date;

}
