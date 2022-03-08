package com.apirest.apirest.service;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface EmailService {
    void send(String from, String to, String title, String body);
    public void sendSimpleMessage(String to, String subject, String text);
    public void sendMessageWithAttachment( String subject, String text, File fileOutput);
    public void createPdfAndSend(String templateName,Map <String,String> ticketData) throws DocumentException, IOException, DocumentException;
    public Map <String,String> generateTicket(int idBoardingPass) throws DocumentException, IOException;

}
