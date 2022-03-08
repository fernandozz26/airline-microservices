package com.apirest.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.apirest.apirest.entities.Flight;
import com.apirest.apirest.entities.Seat;

@Repository
public interface SeatDao extends JpaRepository<Seat,Integer> {
    public List<Seat> findByFlight(Flight flight);

    @Query("SELECT s from Seat s WHERE s.row= ?1 AND s.column= ?2 AND s.flight = ?3")
    public Seat findSeatByRowColumn(Integer row,String column, Flight flight);

}
