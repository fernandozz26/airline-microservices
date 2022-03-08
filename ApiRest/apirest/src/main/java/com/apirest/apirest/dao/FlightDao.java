package com.apirest.apirest.dao;

import com.apirest.apirest.entities.Flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDao extends JpaRepository<Flight,Integer> {
}
