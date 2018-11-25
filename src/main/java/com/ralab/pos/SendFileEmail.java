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

public class SendFileEmail implements Runnable {

    private String to;
    private String from;
    private String filename;

    public SendFileEmail(String to, String from, String filename) {
        this.to = to;
        this.from = from;
        this.filename = filename;
    }

    public synchronized void run() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(dt);
        java.sql.Date sqldt = new java.sql.Date(dt.getTime());
        boolean not_sent = true;
        while (not_sent) {
            // Recipient's email ID needs to be mentioned.
            to = Utils.getRegEmail();

            // Sender's email ID needs to be mentioned
            from = Utils.getRegEmail();

            // Get system properties
            Properties props = System.getProperties();

            // Setup mail server
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            Authenticator auth = new SMTPAuthenticator();
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
                message.setSubject(Utils.getCompanyName() + " " 
                        + " backup " + currentDate);

                // Create the message part 
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText("This is message body");

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
                System.out.println("Backup successfull....");
                try {
                    String stmt_str = "UPDATE bill_no SET backup_done = ? "
                            + "WHERE date = ?";
                    PreparedStatement pstmt = Utils
                            .getConnection()
                            .prepareStatement(stmt_str);
                    pstmt.setBoolean(1, true);
                    pstmt.setDate(2, sqldt);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    
                    e.printStackTrace();
                }
                not_sent = false;
            } catch (MessagingException mex) {
                System.out.println("Backup failed..please try again..");

            }
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public javax.mail.PasswordAuthentication getPasswordAuthentication() {
            String username = Utils.getRegEmail();
            String password = Utils.getRegPwd();
            return new javax.mail.PasswordAuthentication(username, password);
        }
    }
}
