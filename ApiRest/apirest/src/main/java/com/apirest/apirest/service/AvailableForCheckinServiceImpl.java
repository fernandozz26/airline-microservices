package com.apirest.apirest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import com.apirest.apirest.dao.*;
import com.apirest.apirest.entities.BoardingPass;
import com.apirest.apirest.entities.Flight;
import com.apirest.apirest.entities.Passenger;
import com.apirest.apirest.entities.Reservation;
import com.apirest.apirest.entities.Seat;

@Service
public class AvailableForCheckinServiceImpl implements AvailableForCheckinService {
    @Autowired
    ReservationDao reservationDao;
    @Autowired
    FlightDao flightDao;
    @Autowired
    SeatDao seatDao;
    @Autowired
    BoardingPassDao boardingPassDao;


    //Input validation.
    //If null, not available
    @Override
    public Reservation validateWithCode(String last_name, String reservationCode) {

        int id = GetId(reservationCode);
        Reservation reservation = reservationDao.findById(id).orElse(null);

        if (reservation == null) {

            return null;
        }

        if (reservation.getPassenger().getLastName().equals(last_name)) {

            
            if(checkAvailability(reservation)){

                
                return reservation;
            }

        }

        return null;
    }

    //Reservation code to Reservation id convertion
    private int GetId(String reservationCode) {
        //Format: FL-3581
        int id = 0;
        if (reservationCode.length() < 7) {
            return 0;
        }
        try {
            id = Integer.parseInt(reservationCode.substring(3));
            id -= 3580;

        } catch (NumberFormatException ex) {
            return 0;
        }
        return id;
    }

    private Boolean checkAvailability(Reservation reservation){
        Flight flight=reservation.getFlight();
        Date departure_time=flight.getDepartureDate();

        Date now=new Date();
        long difference_In_Time
                = departure_time.getTime() - now.getTime();
        long difference_In_Hours
                = (difference_In_Time
                / (1000 * 60 * 60));
        if(difference_In_Hours>48)
            return false;
        return true;
    }
    @Override
    public String getFlightCode(Reservation reservation) {
        Flight flight=reservation.getFlight();
        int flightid=3580+ flight.getIdFlight();
        String flightCode="FL-"+flightid;
        return flightCode;
    }

    @Override
    public String getPassengerName(Reservation reservation) {
        Passenger passenger=reservation.getPassenger();
        String passengerName=passenger.getName()+" "+passenger.getLastName();
        return passengerName;
    }


    @Override
    public List<Seat> getSeatsList(int idFlight) {
        Flight flight=flightDao.getById(idFlight);
        return seatDao.findByFlight(flight);
    }

    @Override
    public Boolean boardingpassExists(Reservation reservation) {
        BoardingPass boardingPass=boardingPassDao.findByReservation(reservation);
        if(boardingPass==null){
            return false;
        }
        return true;
    }


}