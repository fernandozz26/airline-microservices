package com.apirest.apirest.service;

import java.util.Map;

import com.apirest.apirest.entities.BoardingPass;
import com.apirest.apirest.entities.Reservation;

//Seat selection and boarding pass generator
public interface SeatSelectionService {

    public Map <String,String> getTicketInfo(int idBoardingPass);
    public BoardingPass generateBoardingPass(int reservationId,int row,String column);
    public BoardingPass getBoardingPass(Reservation reservation);



}
