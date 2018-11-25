/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.element.Table;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.lang.Math.abs;

/**
 *
 * @author lab149
 */
public class VariableHeaderEventHandler implements IEventHandler {

    protected String header;
    protected int yPos;
    protected int tYPos;
    protected int tXPos;
    protected boolean isInvoice;
    boolean first_time;
    Table t;
    int type;
    char [] type_char = {'o', 'd', 't', 'n'};
    public VariableHeaderEventHandler(boolean isLandscape,
            boolean isInvoice, Table t, int type) {
        this.isInvoice = isInvoice;
        this.t = t;
        first_time = true;
        if (isLandscape) {
            yPos = 540;
            tYPos = 560;
            tXPos = 250;
        } else {
            yPos = 770;
            tYPos = 800;
            tXPos = 190;

        }
        this.type = type; 
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        try {
            ImageData imgData = ImageDataFactory.create(
                    getClass().getResource("/images/" + Utils.getCompanyLogo()), false);
            
           ImageData imgData1 = ImageDataFactory.create(
                    getClass().getResource("/images/" + type_char[type] + ".png"), false);

            PdfCanvas p = new PdfCanvas(documentEvent.getPage());
    
            p.beginText()
                    .setFontAndSize(PdfFontFactory.createFont(
                            FontConstants.HELVETICA_BOLD), 14)
                    .moveText(tXPos, tYPos)
                    .showText(header)
                    .endText()
                    .stroke()
                    .addImage(imgData, 20, yPos, true);
                    p.addImage(imgData1, 470, yPos + 30, true);

            //find center
            Graphics g ;
            BufferedImage buffer = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB_PRE);
                g = buffer.createGraphics();
            Font f1 = new Font("Helvetica", Font.BOLD, 14);
            Font f2 = new Font("Helvetica", Font.PLAIN, 9);
           int length =  g.getFontMetrics(f1).stringWidth(header);
            
            int len_addr1 = g.getFontMetrics(f2).stringWidth(
                    Utils.getAddressLine1() + 
                            Utils.getAddressLine2());
            int len_pno =  g.getFontMetrics(f2).stringWidth("Phone: " + Utils.getPhoneNo());
            int len_gst = g.getFontMetrics(f2).stringWidth("GSTIN: " + Utils.getGstNo());

            int a1XPos = tXPos - ((length - len_addr1) < 0? abs(len_addr1 - length) / 2 :
                    -1 * abs(len_addr1 - length) / 2);
    
         
            int a1YPos = tYPos - 15;

            int pXPos = tXPos - ((length - len_pno) < 0? abs(len_pno - length) / 2 :
                    -1 * abs(len_pno - length) / 2);
            int pYPos = a1YPos - 10;

            int gXPos = tXPos - ((length - len_gst) < 0? abs(len_gst - length) / 2 :
                    -1 * abs(len_gst - length) / 2) - 10; 
            int gYPos = pYPos - 10;
            
            p.beginText()
                    .setFontAndSize(PdfFontFactory.createFont(
                            FontConstants.HELVETICA), 9)
                    .moveText(a1XPos, a1YPos)
                    .showText(Utils.getAddressLine1() + ", " + Utils.getAddressLine2())
                    .endText()
                    .stroke();
            
            p.beginText()
                    .setFontAndSize(PdfFontFactory.createFont(
                            FontConstants.HELVETICA), 9)
                    .moveText(pXPos, pYPos)
                    .showText("Phone: " + Utils.getPhoneNo())
                    .endText()
                    .stroke();
//            p.beginText()
//                    .setFontAndSize(PdfFontFactory.createFont(
//                            FontConstants.HELVETICA), 9)
//                    .moveText(gXPos, gYPos)
//                    .showText("GSTIN: " + Utils.getGstNo())
//                    .endText()
//                    .stroke();
            if (isInvoice) {
                if (!first_time) {
                    //  Paragraph o0 = new Paragraph("Invoice: ")
                    // .add("..continued").setFontColor(Color.BLACK);
                    // Paragraph o1 = new Paragraph(" ")
                    //.add(" ").add(" ").setFontSize(9).setFontColor(Color.BLACK); 
                    //new Document(documentEvent.getPage().getDocument())
                    //      .add(t);
                } else {
                    first_time = false;
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
