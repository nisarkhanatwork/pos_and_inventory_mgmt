/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  com.ralab.pos;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

/**
 *
 * @author lab149
 */
    public  class AddressHeader implements IEventHandler {

        protected String header;
        protected int yPos ;
        protected int tYPos;
        protected int tXPos;
        protected boolean isInvoice ;
         boolean first_time ;
        Table t;
        public AddressHeader(boolean isLandscape,
                boolean isInvoice, Table t){
            this.isInvoice = isInvoice;
            this.t = t;
            first_time = true;
            if(isLandscape){
                yPos = 540;
                tYPos = 560;
                tXPos = 250;
            } else {
                yPos = 770;
                tYPos = 800;
                tXPos = 190;

            }
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

               PdfCanvas p =  new PdfCanvas(documentEvent.getPage());
                        p.beginText()
                        .setFontAndSize(PdfFontFactory.createFont(
                                FontConstants.HELVETICA_BOLD), 14)
                        .moveText(tXPos, tYPos)
                        .showText(header)
                        .endText()
                        .stroke().addImage(imgData, 20, yPos, true);
                if(isInvoice){
                    if(!first_time){
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
