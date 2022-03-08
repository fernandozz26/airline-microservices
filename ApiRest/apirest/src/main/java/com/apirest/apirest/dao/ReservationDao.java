package com.apirest.apirest.dao;

import com.apirest.apirest.entities.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDao extends JpaRepository<Reservation,Integer> {

}
