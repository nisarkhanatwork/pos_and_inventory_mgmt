/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javafx.scene.control.Alert;
import javax.activation.*;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail implements Runnable {

    private String to;
    private String from;
    private String from_pwd;

    private String filename;

    public SendEmail(String to, String from, String from_pwd, String filename) {
        this.to = to;
        this.from = from;
        this.from_pwd = from_pwd;
        this.filename = filename;
    }

    public synchronized void run() {
        java.util.Date dt = new java.util.Date();
        
        java.sql.Date sqldt = new java.sql.Date(dt.getTime());
        boolean not_sent = true;
        while (not_sent) {
            // Get system properties
            Properties props = System.getProperties();

            // Setup mail server
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Authenticator auth = new SMTPAuthenticator(from, from_pwd);
            // Get the default Session object.
            Session session = Session.getDefaultInstance(props, auth);

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject(Utils.getCompanyName());

                // Create the message part 
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText("Only for verification purposes...");
                

                // Create a multipar message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart);

                // Send message
                Transport.send(message);
                System.out.println("Waiting for registration");
                
            } catch (MessagingException mex) {
                System.out.println("Registration processed failed");

            }
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        private String username;
        private String password;
        public SMTPAuthenticator(String u, String p){
            username = u;
            password = p;
        }
        public javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(username, password);
        }
    }
}
