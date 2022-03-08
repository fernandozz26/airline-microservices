package com.apirest.apirest.dao;

import com.apirest.apirest.entities.Passenger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerDao extends JpaRepository<Passenger,Integer> {
}
