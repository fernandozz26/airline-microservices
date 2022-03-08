package com.apirest.apirest.controller;

import com.apirest.apirest.entities.BoardingPass;
import com.apirest.apirest.entities.Reservation;
import com.apirest.apirest.entities.Seat;
import com.apirest.apirest.service.AvailableForCheckinService;
import com.apirest.apirest.service.EmailService;
import com.apirest.apirest.service.MessageError;
import com.apirest.apirest.service.SeatSelectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CheckInController {

    @Autowired
    AvailableForCheckinService availableForCheckinService;
    @Autowired
    SeatSelectionService seatSelectionService;
    @Autowired
    EmailService emailService;


    @GetMapping(value = "/checkin", params = {"name","reservationCode"})
    public ResponseEntity<Reservation> checkin(@RequestParam String name , @RequestParam String reservationCode) {
        Reservation reservation = availableForCheckinService.validateWithCode(name,reservationCode);
        if(reservation.equals(null)){
            return ResponseEntity.noContent().build();
        }   
        return ResponseEntity.ok(reservation);
    }


    @GetMapping("/checkin/{name}/{code}")
    public ResponseEntity<Map<String, Object>> checkin2(@PathVariable("name")String name,@PathVariable("code")String code) {
        
        Reservation reservation=availableForCheckinService.validateWithCode(name,code);

        if(reservation.equals(null)){
            return ResponseEntity.noContent().build();
        }else{
            int flightId=reservation.getFlight().getIdFlight();
            int reservationId=reservation.getIdReservation();
            String passenger= availableForCheckinService.getPassengerName(reservation);
            String flight= availableForCheckinService.getFlightCode(reservation);
            List<Seat> seats=availableForCheckinService.getSeatsList(flightId);
            double rows = Math.ceil(((double) seats.size() / 4));
            Map<String,Object> res = new HashMap<>();
            res.put("passenger", passenger);
            res.put("rows",(int) Math.round(rows));
            res.put("flight", flight);
            res.put("seats", seats);
            res.put("idReservation", reservationId);
    
            return ResponseEntity.ok(res);
        }

    }

    //@ResponseBody
    @GetMapping(value = "/checkin/boardingpass/{idReservation}/{seat}")
    public ResponseEntity<Map<String, String>>boardingpass(@PathVariable("idReservation") int idReservation, @PathVariable("seat") String seat) {
        int row = Character.getNumericValue(seat.charAt(0));
        String column = String.valueOf(seat.charAt(1));
        BoardingPass boardingPass = seatSelectionService.generateBoardingPass(idReservation,row,column);
        if(boardingPass.equals(null)){
            return ResponseEntity.noContent().build();
        }else{
            Map<String,String> ticketData=seatSelectionService.getTicketInfo(boardingPass.getIdBoardingPass());
        return ResponseEntity.ok(ticketData);
        }
        
    }

    /** 
    @GetMapping("/checkin/send/email/{idBoardingPass}")
    public String sendTicket(@PathVariable("idBoardingPass") int idBoardingPass) throws DocumentException, IOException {
        emailService.generateTicket(idBoardingPass);
        
        return "redirect:/";
    }
**/
    @PostMapping( value = "/checkin/send/email", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})

    public ResponseEntity<String> sentTicket(@RequestBody BoardingPass boardingPass, BindingResult result) throws DocumentException, IOException{
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        emailService.generateTicket(boardingPass.getIdBoardingPass());
        String response = "OK";
        return ResponseEntity.ok(response);
    }

    private String formatMessage(BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
        .map(err -> {
            Map<String, String> error = new HashMap<>();
            error.put(err.getField(), err.getDefaultMessage());
            return error;
        }).collect(Collectors.toList());

        MessageError errorMesage = MessageError.builder().code("01").messages(errors).build();
        
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(errorMesage);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return json;
    }

}
