package com.apirest.apirest.service;

import com.apirest.apirest.dao.*;
import com.apirest.apirest.entities.*;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
    private BoardingPassDao boardingPassDao;

    @Autowired
    ClassLoaderTemplateResolver templateResolver;






    @Override
    public Map <String,String> generateTicket(int idBoardingPass) throws DocumentException, IOException {
        //Carga de todas las clases necesarias
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
        ticketData.put("flight","FL-15"+flight.getIdFlight());

        createPdfAndSend("ticket",ticketData);



        return ticketData;

    }





    @Override
    public void send(String from, String to, String title, String body) {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        try {
            if (from != null) {
                mimeMessageHelper.setFrom(from);
            }
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(body);
            mimeMessageHelper.setTo(to);
            this.javaMailSender.send(message);
        } catch (MessagingException messageException) {
            throw new RuntimeException(messageException);
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("airlinescielo.boardingpass@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }

    @Override
    public void sendMessageWithAttachment(String to, String text, File fileOutput) {
        MimeMessage message = javaMailSender.createMimeMessage();


        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("airlinescielo.boardingpass@gmail.com");
            helper.setTo(to);
            helper.setSubject("Cielo BoardingPass");
            helper.setText(text);
            /*
            FileSystemResource file
                    = new FileSystemResource(fileOutput);

             */
            helper.addAttachment("BoardingPass.pdf", fileOutput);

            javaMailSender.send(message);
        } catch (MessagingException messageException) {
            throw new RuntimeException(messageException);
        }
    }

    @Override
    public void createPdfAndSend(String templateName,Map <String,String> ticketData) throws IOException, DocumentException {
        //TemplateXHTML to StringHTML
        /*
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

         */

        Context context=new Context();
        context.setVariable("seatNo",ticketData.get("seatNo"));
        context.setVariable("passengerName",ticketData.get("passengerName"));
        context.setVariable("passport",ticketData.get("passport"));
        context.setVariable("email", ticketData.get("email"));
        context.setVariable("bGate",ticketData.get("bGate"));
        context.setVariable("depart",ticketData.get("depart"));
        context.setVariable("arrive",ticketData.get("arrive"));
        context.setVariable("emergencyD",ticketData.get("emergencyD"));
        context.setVariable("pos",ticketData.get("pos"));
        context.setVariable("origin",ticketData.get("origin"));
        context.setVariable("destiny",ticketData.get("destiny"));
        context.setVariable("flight",ticketData.get("flight"));

        String html = templateEngine.process(templateName, context);
        //StringHTML to pdf file
        File file = new File("ticket.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        //Send the pdf through mail
        String text="Estimado pasajero "+ticketData.get("passengerName") +", se adjunta el boarding-pass para su sig vuelo." +"\n\nAgradecemos su preferencia" +"\n\nAerolinia Cielo";
        //Mandar el mail
        sendMessageWithAttachment(ticketData.get("email"), text, file);


    }


}
