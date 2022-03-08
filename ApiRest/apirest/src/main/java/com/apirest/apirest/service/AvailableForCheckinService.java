package com.apirest.apirest.service;


import java.util.List;

import com.apirest.apirest.entities.Reservation;
import com.apirest.apirest.entities.Seat;

public interface AvailableForCheckinService {
    public Reservation validateWithCode(String last_name, String reservationCode);
    public String getFlightCode(Reservation reservation);
    public String getPassengerName(Reservation reservation);
    public List<Seat> getSeatsList(int idFlight);
    public Boolean boardingpassExists(Reservation reservation);
}
