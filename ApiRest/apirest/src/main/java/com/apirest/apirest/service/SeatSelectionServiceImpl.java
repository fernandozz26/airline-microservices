package com.apirest.apirest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.apirest.apirest.dao.*;
import com.apirest.apirest.entities.*;

@Service
public class SeatSelectionServiceImpl implements  SeatSelectionService{

    @Autowired
    ReservationDao reservationDao;
    @Autowired
    BoardingPassDao boardingPassDao;
    @Autowired
    SeatDao seatDao;





    @Override
    public Map<String, String> getTicketInfo(int idBoardingPass) {
        BoardingPass boardingPass=boardingPassDao.findById(idBoardingPass).orElse(null);
        Reservation reservation=boardingPass.getReservation();
        Seat seat= boardingPass.getSeat();
        Flight flight=reservation.getFlight();
        Passenger passenger=reservation.getPassenger();
        BoardingGate gate= flight.getBoardingGate();

        Map <String,String> ticketData= new HashMap<String,String>();

        //Sumary
        ticketData.put("seatNo",seat.getRow()+" "+seat.getColumn());
        ticketData.put("passengerName", passenger.getName()+" "+passenger.getLastName());
        ticketData.put("passport",passenger.getPassport());
        ticketData.put("email", passenger.getEmail());
        ticketData.put("bGate",gate.getGate());
        ticketData.put("depart",flight.getDepartureDate()+"");
        ticketData.put("arrive",flight.getArrivalDate()+"");
        ticketData.put("emergencyD",seat.getEmergencyNear()?"SI":"NO");
        ticketData.put("pos",seat.getType());
        ticketData.put("origin",flight.getOrigin());
        ticketData.put("destiny",flight.getDestiny());
        ticketData.put("vuelo","FL-15"+flight.getIdFlight());

        return ticketData;
    }

    @Override
    public BoardingPass generateBoardingPass(int reservationId, int row, String column) {
        Reservation reservation=reservationDao.getById(reservationId);

        Seat seat=seatDao.findSeatByRowColumn(row,column,reservation.getFlight());
        seat.setStatus("OCCUPIED");
        if(seat.equals(null)){
            return null;
        }
        seatDao.save(seat);
        BoardingPass boardingPass= new BoardingPass();
        boardingPass.setSeat(seat);
        boardingPass.setReservation(reservation);

        return  boardingPassDao.save(boardingPass);
    }



    @Override
    public BoardingPass getBoardingPass(Reservation reservation) {
        return boardingPassDao.findByReservation(reservation);
    }



}
