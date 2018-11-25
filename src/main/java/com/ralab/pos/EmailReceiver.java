/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

//import org.apache.log4j.Logger;


/**
 * This program demonstrates how to download e-mail messages and save
 * attachments into files on disk.
 *
 * @author simbu
 *
 */
public class EmailReceiver {

    /**
     * log4j Logger
     */
    //private static Logger log = Logger.getLogger(EmailReceiver.class);

    private static String saveDirectory = "/home/content"; // directory to save the downloaded documents

    /**
     * Sets the directory where attached files will be stored.
     *
     * @param dir absolute path of the directory
     */
    public void setSaveDirectory(String dir) {
        EmailReceiver.saveDirectory = dir;
    }

    /**
     * Downloads new messages and saves attachments to disk if any.
     *
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    public static String downloadEmailAttachments(String host, String port, String userName, String password) {
        Properties properties = new Properties();

        // server setting
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);

        // SSL setting
        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore("pop3");
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();

                String contentType = message.getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";

                if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
               messageContent = message.getSubject();
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message : " + subject);
                
                if (from.indexOf("masterserver.yourlab@gmail.com") >=0 ) {
                  
                    if (AES.encrypt(userName + Utils.getCompanyName(),
                            Utils.getAESKey()).equals(subject)) {

                        try {
                            String stmt_str = "INSERT INTO keytbl (userid, password, mykey) "
                   
                                    + " VALUES (?, ?, ?)";
                            PreparedStatement pstmt = Utils
                                    .getJaasDBCon()
                                    .prepareStatement(stmt_str);
                            pstmt.setString(1, userName);
                            pstmt.setString(2, password);
                            pstmt.setString(3, messageContent);

                            pstmt.executeUpdate();
                            
                            System.out.println("\t\t User registered...");
                            return from;
                        } catch (SQLException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
           // log.error(ex);
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
           // log.error(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
           // log.error(ex);
        }
        return null;
    }
}
