/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class ReportsController implements Initializable {

    @FXML
    private DatePicker day1;
    @FXML
    private DatePicker day2;

    @FXML
    private RadioButton ic_report;
    @FXML
    private RadioButton sales_report;
    @FXML
    private RadioButton lr_report;

    private int size_of_rep = 0;
    private Table[] table;

    private Table sales_table;
    private Table sales_table_red;

    private Table lr_table;

    ToggleGroup group;

    boolean ic_gen = false;

    static final int CGST2_5 = 1;
    static final int CGST6 = 2;
    static final int CGST9 = 3;
    static final int CGST14 = 4;
    static final int SGST2_5 = 5;
    static final int SGST6 = 6;
    static final int SGST9 = 7;
    static final int SGST14 = 8;
    static final int IGST5 = 9;
    static final int IGST12 = 10;
    static final int IGST18 = 11;
    static final int IGST28 = 12;

    static final int AMOUNT = 13;
    static final int TAX = 14;
    static final int TAMOUNT = 15;

    static final int RED_AMOUNT = 1;
    static final int RED_TAX = 2;
    static final int RED_TAMOUNT = 3;
    int rep_type = 0;
    BigDecimal[] final_result = new BigDecimal[16];
    BigDecimal[] final_result_red = new BigDecimal[4];

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        group = new ToggleGroup();

        ic_report.setToggleGroup(group);
        sales_report.setToggleGroup(group);
        lr_report.setToggleGroup(group);

        sales_report.setSelected(true);

        ic_report.setUserData("ic");
        sales_report.setUserData("sales");
        lr_report.setUserData("lr");

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if (group
                            .getSelectedToggle()
                            .getUserData()
                            .toString().equals("ic")) {
                        rep_type = 1;

                    } else if (group
                            .getSelectedToggle()
                            .getUserData()
                            .toString().equals("lr")) {
                        rep_type = 2;
                    } else {
                        rep_type = 0;
                    }
                }
            }
        });
        Arrays.fill(final_result, BigDecimal.ZERO);
    }

    @FXML
    private void handleGENERATEButtonAction(ActionEvent event) {
        if (day1.getValue() == null || day2.getValue() == null) {
            //Alert Error Message
        } else {

            switch (rep_type) {
                case 0:
                    if (Utils.isRedBilling()) {
                        sales_report_red(day1.getValue(), day2.getValue());
                    } else {
                        sales_report(day1.getValue(), day2.getValue());
                    }
                    break;
                case 1:
                    callIText(day1.getValue(), day2.getValue(), true);
                    callIText(day1.getValue(), day2.getValue(), false);
                    break;
                case 2:
                    lr_report(day1.getValue(), day2.getValue());
                    break;
            }

        }
    }

    private void callIText(LocalDate d1, LocalDate d2, boolean is_quant) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("HH_mm_ss");
        String currentTime = sdf.format(dt);
        String file_no = new String("1");
        String data;

        if (is_quant) {
            data = "quant";
        } else {
            data = "amount";
        }

        if (addItems(is_quant) == null) {
            printNoDataPdf(d1, d2);
            return;
        }
        for (int i = 0; i < table.length; i++) {

            String dest = Utils.getRepDir() + "Item_Wise_Cust_Wise_Report" + d1 + " -to-"
                    + d2 + "_" + currentTime
                    + "_" + data + "_" + file_no + ".pdf";

            try {
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

                //For page header
                VariableHeaderEventHandler handler = new VariableHeaderEventHandler(
                        true, false, null, 3);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

                Document doc = new Document(pdfDoc, PageSize.A4.rotate());
                doc.setMargins(60, 20, 20, 20);

                //For page header
                handler.setHeader(Utils.getCompanyName() + String.format(
                        " Item wise report\n (From: %s To: %s)\n", d1, d2));

                doc.add(table[i]);

                doc.close();
                doc = null;
                pdfDoc = null;
                handler = null;
                file_no = String.valueOf(Integer.parseInt(file_no) + 1);
            } catch (Exception e) {
                Alert error41 = new Alert(Alert.AlertType.INFORMATION);
                error41.setContentText("callIText " + e.getMessage());
                e.printStackTrace(System.out);
                error41.showAndWait();
            }

        }
    }

    private Table[] addItems(boolean is_quant) {

        double[][] amount = new double[1][1];
        LinkedHashMap<String, Integer> prod = new LinkedHashMap();
        LinkedHashMap<String, Integer> cust = new LinkedHashMap();
        String data;

        if (is_quant) {
            data = "quant";
        } else {
            data = "amount";
        }
        try {
            Statement stmt = Utils.getConnection().createStatement();
            Statement stmt1 = Utils.getConnection().createStatement();
            Statement stmt2 = Utils.getConnection().createStatement();

            ResultSet rs = stmt.executeQuery("SELECT  p.mfg, p.prod,"
                    + " rep.prod_code, rep.phone_num, rep.a FROM (SELECT "
                    + "prod_code, phone_num,SUM(" + data + ") as a FROM bills b "
                    + "WHERE Date(b.date_time) BETWEEN \'"
                    + day1.getValue() + "\'"
                    + " AND \'"
                    + day2.getValue() + "\'  GROUP by phone_num, prod_code HAVING prod_code <> \'\')"
                    + " as rep  INNER JOIN products p ON "
                    + "rep.prod_code = p.prod_code "
            );

            ResultSet rs2 = stmt2.executeQuery("SELECT  COUNT(DISTINCT prod_code)"
                    + " AS prod_code_no, "
                    + "COUNT( DISTINCT phone_num) AS phone_num_no "
                    + "FROM bills  "
                    + "WHERE Date(date_time) BETWEEN \'"
                    + day1.getValue() + "\'"
                    + " AND \'"
                    + day2.getValue() + "\' "
            );
            while (rs2.next()) {
                try {
                    int rows = rs2.getInt("prod_code_no");
                    int cols = rs2.getInt("phone_num_no");
                    amount = new double[rows][cols];
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setContentText("addItems 0" + e.getMessage());
                    e.printStackTrace(System.out);
                    error.showAndWait();

                }
            }
            int col_no = 0;
            int row_no = 0;
            while (rs.next()) {
                try {

                    String mfg = rs.getString("p.mfg");
                    String prd = rs.getString("p.prod");
                    String cd = rs.getString("rep.prod_code");
                    String phn = rs.getString("rep.phone_num");
                    Double amnt = rs.getDouble("rep.a");

                    mfg = mfg.substring(0, mfg.length() < 10 ? mfg.length() : 10);
                    prd = prd.substring(0, prd.length() < 10 ? prd.length() : 10);

                    String row_particulars = mfg + "," + prd + "(" + cd + ")";

                    if (!prod.containsKey(row_particulars)) {
                        prod.put(row_particulars, row_no++);
                    }

                    ResultSet rs1 = stmt1.executeQuery("SELECT party_name "
                            + "FROM ADDRESS WHERE id = \'"
                            + phn
                            + "\'");
                    String name = new String();
                    if (rs1.next()) {
                        name = rs1.getString("party_name");

                        name = name.substring(0, name.length() < 10 ? name.length() : 10);
                    }
                    String col_particulars = name + "\n" + phn;

                    if (!cust.containsKey(col_particulars)) {
                        cust.put(col_particulars, col_no++);
                    }

                    amount[prod.get(row_particulars)][cust.get(col_particulars)] = amnt;

                } catch (SQLException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setContentText("addItems 1" + e.getMessage());
                    e.printStackTrace(System.out);
                    error.showAndWait();

                }

            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("addItems 2" + e.getMessage());
            e.printStackTrace(System.out);
            error.showAndWait();
        }

        if (cust.size() == 0) {
            return null;
        }
        float[] columnWidths;
        float[] columnWidths1;
        float[] columnWidths_sep_page;

        int row_len = cust.size();
        int no_of_tables = row_len / 8 + (((row_len % 8) > 0) ? 1 : 0);
        int remainder = (row_len % 8);
        boolean sep_page_4_total = (remainder == 0) ? true : false;

        size_of_rep = cust.size();

        int t_no = no_of_tables + (sep_page_4_total ? 1 : 0);
        table = new Table[t_no];

        columnWidths = new float[9];
        columnWidths[0] = 30;
        for (int i = 1; i <= 8; i++) {
            columnWidths[i] = 10;

        }

        columnWidths1 = new float[2 + remainder]; // particulars + Total + remaining
        columnWidths[0] = 30;
        for (int i = 1; i <= remainder + 1; i++) {
            columnWidths[i] = 10;

        }
        columnWidths_sep_page = new float[2];
        columnWidths_sep_page[0] = 30;
        columnWidths_sep_page[1] = 10;

        int k = 0;
        for (; k < no_of_tables - (remainder == 0 ? 0 : 1); k++) {
            table[k] = new Table(columnWidths);

            table[k].setWidthPercent(100);

            table[k].setFontSize(9);
        }
        if (remainder != 0) {
            table[k] = new Table(columnWidths1);

            table[k].setWidthPercent(100);

            table[k].setFontSize(9);
        }
        if (sep_page_4_total && (no_of_tables != 0)) {
            table[k] = new Table(columnWidths_sep_page);

            table[k].setWidthPercent(100);

            table[k].setFontSize(9);

        }

        try {

            for (k = 0; k < no_of_tables; k++) {

                PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
                Cell cell1 = new Cell(1, 1)
                        .add(new Paragraph("Particulars"))
                        .setFont(f)
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                table[k].addHeaderCell(cell1);
            }

            int i = 0;

            for (HashMap.Entry e : cust.entrySet()) {
                PdfFont f3 = PdfFontFactory.createFont(FontConstants.HELVETICA);

                Cell cell_header = new Cell(1, 1)
                        .add(new Paragraph((String) e.getKey()))
                        .setFont(f3)
                        .setBold()
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                table[i / 8].addHeaderCell(cell_header);
                i = i + 1;
//                if (i % 8 == 0) {
//                    PdfFont f1 = PdfFontFactory.createFont(FontConstants.HELVETICA);
//                    Cell cell1 = new Cell(1, 1)
//                            .add(new Paragraph("Particulars"))
//                            .setFont(f1)
//                            .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                    table[i / 8].addHeaderCell(cell1);
//                }
            }

            if (sep_page_4_total) {
                PdfFont f4 = PdfFontFactory.createFont(FontConstants.HELVETICA);
                Cell cell11 = new Cell(1, 1)
                        .add(new Paragraph("Particulars"))
                        .setFont(f4)
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                table[no_of_tables].addHeaderCell(cell11);

                //TODO add total column
            }
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell1 = new Cell(1, 1)
                    .add(new Paragraph("Total(Item wise)"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLUE);
            table[no_of_tables - (sep_page_4_total ? 0 : 1)].addHeaderCell(cell1);
            int row_count = 0;
            for (HashMap.Entry e : prod.entrySet()) {
                i = 0;

                if (i % 8 == 0) {
                    PdfFont f5 = PdfFontFactory.createFont(FontConstants.HELVETICA);

                    Cell cell_header = new Cell(1, 1)
                            .add(new Paragraph((String) e.getKey()))
                            .setFont(f5)
                            .setBold()
                            .setFontSize(9).setBold().setFontColor(Color.BLACK);
                    table[i / 8].addCell(cell_header);
                }
                for (int m = 0; m < cust.size(); m++) {
                    PdfFont f6 = PdfFontFactory.createFont(FontConstants.HELVETICA);
                    Cell cell_temp = new Cell(1, 1)
                            .add(new Paragraph(String.valueOf(amount[row_count][m])))
                            .setFont(f6)
                            .setBold()
                            .setFontSize(9).setBold().setFontColor(Color.BLACK);
                    table[i / 8].addCell(cell_temp);
                    i = i + 1;
                    if (i % 8 == 0) {
                        PdfFont f1 = PdfFontFactory.createFont(FontConstants.HELVETICA);

                        Cell cell_header = new Cell(1, 1)
                                .add(new Paragraph((String) e.getKey()))
                                .setFont(f1)
                                .setBold()
                                .setFontSize(9).setBold().setFontColor(Color.BLACK);
                        table[i / 8].addCell(cell_header);
                    }

                }
                //TODO add total item quant
                //find total row count
                BigDecimal b = BigDecimal.ZERO;

                for (int h = 0; h < amount[row_count].length; h++) {
                    b = BigDecimal.valueOf(amount[row_count][h]).add(b);
                }
                PdfFont f6 = PdfFontFactory.createFont(FontConstants.HELVETICA);
                Cell cell_temp = new Cell(1, 1)
                        .add(new Paragraph(b.toString()))
                        .setFont(f6)
                        .setBold()
                        .setFontSize(9).setBold().setFontColor(Color.BLUE);
                table[i / 8].addCell(cell_temp);

                row_count++;

            }
            row_count--;
            //TODO add total customer wise
            i = 0;

            if (i % 8 == 0) {
                PdfFont f15 = PdfFontFactory.createFont(FontConstants.HELVETICA);

                Cell cell_header = new Cell(1, 1)
                        .add(new Paragraph("Total(Customer wise)"))
                        .setFont(f15)
                        .setBold()
                        .setFontSize(9).setBold().setFontColor(Color.BLUE);
                table[i / 8].addCell(cell_header);
            }
            for (int c = 0; c < cust.size(); c++) {
                BigDecimal b = BigDecimal.ZERO;

                for (int p = 0; p < amount.length; p++) {
                    b = BigDecimal.valueOf(amount[p][c]).add(b);

                }

                PdfFont f16 = PdfFontFactory.createFont(FontConstants.HELVETICA);
                Cell cell_temp = new Cell(1, 1)
                        .add(new Paragraph(String.valueOf(b.toString())))
                        .setFont(f16)
                        .setBold()
                        .setFontSize(9).setBold().setFontColor(Color.BLUE);
                table[i / 8].addCell(cell_temp);
                i = i + 1;
                if (i % 8 == 0) {
                    PdfFont f1 = PdfFontFactory.createFont(FontConstants.HELVETICA);

                    Cell cell_header = new Cell(1, 1)
                            .add(new Paragraph("Total(Customer wise)"))
                            .setFont(f1)
                            .setBold()
                            .setFontSize(9).setBold().setFontColor(Color.BLUE);
                    table[i / 8].addCell(cell_header);
                }

            }
            //TODO add total item quant
            //find total row count
            BigDecimal b = BigDecimal.ZERO;

            for (int h = 0; h < amount.length; h++) {
                for (int g = 0; g < amount[h].length; g++) {

                    b = BigDecimal.valueOf(amount[h][g]).add(b);
                }
            }
            PdfFont f61 = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell_temp = new Cell(1, 1)
                    .add(new Paragraph(is_quant ? "" : b.toString()))
                    .setFont(f61)
                    .setBold()
                    .setFontSize(9).setBold().setFontColor(Color.DARK_GRAY);
            table[i / 8].addCell(cell_temp);

        } catch (Exception e) {
            Alert error6 = new Alert(Alert.AlertType.INFORMATION);
            error6.setContentText("addItems 3" + e.getMessage());
            e.printStackTrace(System.out);
            error6.showAndWait();
        }

        return table;
    }

    private void printNoDataPdf(LocalDate d1, LocalDate d2) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("HH_mm_ss");
        String currentTime = sdf.format(dt);

        String dest = Utils.getRepDir() + "Item_Wise_Cust_Wise_Report" + d1 + " -to-" + d2 + currentTime
                + "FOUND_NO_DATA.pdf";
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

            //For page header
            VariableHeaderEventHandler handler = new VariableHeaderEventHandler(
                    true,
                    false,
                    null,
                    3);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            Document doc = new Document(pdfDoc, PageSize.A4.rotate());
            doc.setMargins(60, 20, 20, 20);

            //For page header
            handler.setHeader(Utils.getCompanyName() + String.format(
                    " Item wise report\n (From: %s To: %s)\n", d1, d2));
            doc.add(new AreaBreak());
            doc.close();
        } catch (Exception e) {
            Alert error4 = new Alert(Alert.AlertType.INFORMATION);
            error4.setContentText("printNoDataPdf " + e.getMessage());
            e.printStackTrace(System.out);
            error4.showAndWait();
        }
    }

    void sales_report(LocalDate d1, LocalDate d2) {

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("HH_mm_ss");
        String currentTime = sdf.format(dt);
        String file_no = new String("1");
        String data;

        if (addSalesRepItems() == null) {
            printNoDataPdf(d1, d2);
            return;
        }
        for (int i = 0; i < 1; i++) {

            String dest = Utils.getRepDir() + "Sales_Report_" + d1 + " -to-"
                    + d2 + "_" + currentTime
                    + "_" + file_no + ".pdf";

            try {
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

                //For page header
                VariableHeaderEventHandler handler = new VariableHeaderEventHandler(
                        true, false, null, 3);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

                Document doc = new Document(pdfDoc, PageSize.A4.rotate());
                doc.setMargins(60, 20, 20, 20);

                //For page header
                handler.setHeader(Utils.getCompanyName() + String.format(
                        " Sales Report \n (From: %s To: %s)\n", d1, d2));

                doc.add(sales_table);

                doc.close();
                doc = null;
                pdfDoc = null;
                handler = null;
                file_no = String.valueOf(Integer.parseInt(file_no) + 1);
            } catch (Exception e) {
                Alert error41 = new Alert(Alert.AlertType.INFORMATION);
                error41.setContentText("callIText " + e.getMessage());
                e.printStackTrace(System.out);
                error41.showAndWait();
            }

        }
    }

    private Table addSalesRepItems() {
        Color bg = new DeviceRgb(244, 244, 244);
        Cell cell1, cell5, cell3, cell4, cell6,
                cell2_1_0, cell2_1_1, cell2_1_2, cell2_1_3,
                cell7_1_0, cell7_1_1, cell7_1_2, cell7_1_3,
                cell8_1_0, cell8_1_1, cell8_1_2, cell8_1_3,
                cell9, cell12, cell13;
        cell1 = cell3 = cell5 = cell4 = cell6
                = cell2_1_0 = cell2_1_1 = cell2_1_2 = cell2_1_3
                = cell7_1_0 = cell7_1_1 = cell7_1_2 = cell7_1_3
                = cell8_1_0 = cell8_1_1 = cell8_1_2 = cell8_1_3
                = cell9 = cell12 = cell13 = new Cell(1, 1);

        float[] col = {4f, 4f, 15f, 15f, 14f,
            4f, 4f, 4f, 4f,
            4f, 4f, 4f, 4f,
            4f, 4f, 4f, 4f,
            10f, 10f, 10f};

        sales_table = new Table(col);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            cell1 = new Cell(1, 1)
                    // .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SNO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell5 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Bill No."))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell3 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Custormer Name"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell4 = new Cell(1, 1)
                    //    .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Town"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell6 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("GSTIN"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell7_1_0 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("CGST 2.5"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell7_1_1 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("CGST 6"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell7_1_2 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("CGST 9"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell7_1_3 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("CGST 14"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell8_1_0 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SGST 2.5"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell8_1_1 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SGST 6"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell8_1_2 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SGST 9"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell8_1_3 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SGST 14"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell2_1_0 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("IGST 5"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell2_1_1 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("IGST 12"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell2_1_2 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("IGST 18"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell2_1_3 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("IGST 28"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

//            cell8 = new Cell(1, 1)
//                    //   .setBorderLeft(Border.NO_BORDER)
//                    //  .setBorderTop(Border.NO_BORDER)
//                    //  .setBorderRight(Border.NO_BORDER)
//                    .add(new Paragraph("Units"))
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                    .setBackgroundColor(bg)
//                    .setFont(f)
//                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell9 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //  .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Tax"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell12 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph(" Taxable \n Amount"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell13 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph(" Total Amount"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            sales_table.addHeaderCell(cell1)
                    .addHeaderCell(cell5)
                    .addHeaderCell(cell3)
                    .addHeaderCell(cell4)
                    .addHeaderCell(cell6)
                    .addHeaderCell(cell7_1_0)
                    .addHeaderCell(cell7_1_1)
                    .addHeaderCell(cell7_1_2)
                    .addHeaderCell(cell7_1_3)
                    .addHeaderCell(cell8_1_0)
                    .addHeaderCell(cell8_1_1)
                    .addHeaderCell(cell8_1_2)
                    .addHeaderCell(cell8_1_3)
                    .addHeaderCell(cell2_1_0)
                    .addHeaderCell(cell2_1_1)
                    .addHeaderCell(cell2_1_2)
                    .addHeaderCell(cell2_1_3)
                    .addHeaderCell(cell12)
                    .addHeaderCell(cell9)
                    .addHeaderCell(cell13);

        } catch (IOException e) {
            e.printStackTrace();
        }

        int no_of_items = 0;
        try {
            String str0 = "SELECT COUNT(DISTINCT bill_no) as cnt"
                    + " FROM bills b WHERE  Date(b.date_time)"
                    + " BETWEEN ? AND ? AND"
                    + " b.cancel <> '1' AND b.deleted <> '1'"
                    + " ";
            PreparedStatement p0 = Utils.getConnection().prepareStatement(str0);
            p0.setDate(1, java.sql.Date.valueOf(day1.getValue()));
            p0.setDate(2, java.sql.Date.valueOf(day2.getValue()));
            ResultSet rs0 = p0.executeQuery();

            if (rs0.next()) {
                no_of_items = rs0.getInt("cnt");
            }
            String str1 = "SELECT *  FROM bills b INNER JOIN "
                    + " ADDRESS a ON a.id=b.phone_num AND  Date(b.date_time)"
                    + " BETWEEN ? AND ? AND"
                    + " b.cancel <> '1' AND b.deleted <> '1'"
                    + " ORDER BY b.bill_no ASC";
            PreparedStatement p1 = Utils.getConnection().prepareStatement(str1);
            p1.setDate(1, java.sql.Date.valueOf(day1.getValue()));
            p1.setDate(2, java.sql.Date.valueOf(day2.getValue()));
            ResultSet rs1 = p1.executeQuery();
            int s_no = 0;
            Color c = Color.WHITE;

            //  ArrayList<ArrayList<BigDecimal>> values = new ArrayList<>();
            //    HashMap<Integer, Integer> hm = new HashMap();
            //     HashMap<Integer, String[]> details = new HashMap();
            int curr_item = 0;
            BigDecimal bcgst2_5, bcgst6, bcgst9, bcgst14,
                    bsgst2_5, bsgst6, bsgst9, bsgst14,
                    bigst5, bigst12, bigst18, bigst28, bamount, btax, btamount,
                    iamount, itax;
            bcgst2_5 = bcgst6 = bcgst9 = bcgst14
                    = bsgst2_5 = bsgst6 = bsgst9 = bsgst14
                    = bigst5 = bigst12 = bigst18 = bigst28
                    = bamount = btax = btamount = iamount = itax = BigDecimal.ZERO;
            String[] info = new String[3];
            boolean first_time = true;
            BigDecimal bbtax = BigDecimal.ZERO;
            Arrays.fill(final_result, BigDecimal.ZERO);
            while (rs1.next()) {
                if (curr_item != rs1.getInt("bill_no")) {
                    if (!first_time) {

                        fill_table(s_no, curr_item, info[0], info[1], info[2],
                                String.valueOf(bcgst2_5), String.valueOf(bcgst6),
                                String.valueOf(bcgst9), String.valueOf(bcgst14),
                                String.valueOf(bsgst2_5), String.valueOf(bsgst6),
                                String.valueOf(bsgst9), String.valueOf(bsgst14),
                                String.valueOf(bigst5), String.valueOf(bigst12),
                                String.valueOf(bigst18), String.valueOf(bigst28),
                                bamount, btax, btamount);

                    } else {
                        first_time = false;

                    }
                    final_result[CGST2_5] = final_result[CGST2_5].add(bcgst2_5);
                    final_result[CGST6] = final_result[CGST6].add(bcgst6);
                    final_result[CGST9] = final_result[CGST9].add(bcgst9);
                    final_result[CGST14] = final_result[CGST14].add(bcgst14);
                    final_result[SGST2_5] = final_result[SGST2_5].add(bsgst2_5);
                    final_result[SGST6] = final_result[SGST6].add(bsgst6);
                    final_result[SGST9] = final_result[SGST9].add(bsgst9);
                    final_result[SGST14] = final_result[SGST14].add(bsgst14);
                    final_result[IGST5] = final_result[IGST5].add(bigst5);
                    final_result[IGST12] = final_result[IGST12].add(bigst12);
                    final_result[IGST18] = final_result[IGST18].add(bigst18);
                    final_result[IGST28] = final_result[IGST28].add(bigst28);

                    final_result[AMOUNT] = final_result[AMOUNT].add(bamount);

                    final_result[TAX] = final_result[TAX].add(btax);
                    final_result[TAMOUNT] = final_result[TAMOUNT].add(btamount);

                    curr_item = rs1.getInt("bill_no");
                    s_no++;
                    // hm.put(curr_item, s_no++);

                    info[0] = new String(rs1.getString("party_name"));
                    info[1] = new String(rs1.getString("town"));
                    info[2] = new String(rs1.getString("tin"));
                    // details.put(hm.get(curr_item), info);
                    bcgst2_5 = bcgst6 = bcgst9 = bcgst14
                            = bsgst2_5 = bsgst6 = bsgst9 = bsgst14
                            = bigst5 = bigst12 = bigst18 = bigst28
                            = bamount = btax = bbtax = btamount = iamount = itax = BigDecimal.ZERO;

                }
//                bcgst2_5 = values.get(s_no).get(CGST2_5);
//                bcgst6 = values.get(s_no).get(CGST6);
//                bcgst9 = values.get(s_no).get(CGST9);
//                bcgst14 = values.get(s_no).get(CGST14);
//                bsgst2_5 = values.get(s_no).get(SGST2_5);
//                bsgst6 = values.get(s_no).get(SGST6);
//                bsgst9 = values.get(s_no).get(SGST9);
//                bsgst14 = values.get(s_no).get(SGST14);
//                bigst5 = values.get(s_no).get(IGST5);
//                bigst12 = values.get(s_no).get(IGST12);
//                bigst18 = values.get(s_no).get(IGST18);
//                bigst28 = values.get(s_no).get(IGST28);

                //for each item
                bbtax = iamount = itax = BigDecimal.ZERO;

                iamount = BigDecimal.valueOf(rs1.getDouble("amount"));
                bamount = bamount.add(iamount);
                // btax = values.get(s_no).get(TAX);
                //    btamount = values.get(s_no).get(TAMOUNT);

                if (rs1.getBoolean("is_igst")) {
                    double tax = rs1.getDouble("gst");
                    bbtax = BigDecimal.valueOf(tax);
                    switch ((int) tax) {
                        case 5:
                            bigst5 = bigst5.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 9:
                            bigst12 = bigst12.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 18:
                            bigst18 = bigst18.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 28:
                            bigst28 = bigst28.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        default:

                            break;

                    }
                    itax = bbtax.multiply(iamount).divide(BigDecimal.valueOf(100.0));
                    btax = btax.add(itax);
                } else {
//                    bcgst2_5 = bcgst6 = bcgst9 = bcgst14
//                            = bsgst2_5 = bsgst6 = bsgst9 = bsgst14
//                            = bigst5 = bigst12 = bigst18 = bigst28
//                            = bamount = btax = btamount = BigDecimal.ZERO;

                    double tax = BigDecimal.valueOf(rs1.getDouble("gst"))
                            .divide(BigDecimal.valueOf(2.0)).doubleValue();
                    bbtax = BigDecimal.valueOf(tax);
                    switch ((int) tax) {
                        case 2:
                            bcgst2_5 = bcgst2_5.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 6:
                            bcgst6 = bcgst6.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 9:
                            bcgst9 = bcgst9.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 14:
                            bcgst14 = bcgst14.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        default:

                            break;

                    }
                    itax = bbtax.multiply(iamount).divide(BigDecimal.valueOf(100.0));

                    tax = BigDecimal.valueOf(rs1.getDouble("gst"))
                            .divide(BigDecimal.valueOf(2.0)).doubleValue();
                    bbtax = BigDecimal.valueOf(tax);
                    switch ((int) tax) {
                        case 2:
                            bsgst2_5 = bsgst2_5.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 6:
                            bsgst6 = bsgst6.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 9:
                            bsgst9 = bsgst9.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        case 14:
                            bsgst14 = bsgst14.add(iamount
                                    .multiply(bbtax)).divide(BigDecimal.valueOf(100.0));
                            break;
                        default:

                            break;

                    }
                    itax = itax.add(bbtax.multiply(iamount).divide(BigDecimal.valueOf(100.0)));

                    btax = btax.add(itax);

                }
                btamount = btamount.add(itax).add(iamount);

            }
            if (s_no != 0) {
                fill_table(s_no, curr_item, info[0], info[1], info[2],
                        String.valueOf(bcgst2_5), String.valueOf(bcgst6),
                        String.valueOf(bcgst9), String.valueOf(bcgst14),
                        String.valueOf(bsgst2_5), String.valueOf(bsgst6),
                        String.valueOf(bsgst9), String.valueOf(bsgst14),
                        String.valueOf(bigst5), String.valueOf(bigst12),
                        String.valueOf(bigst18), String.valueOf(bigst28),
                        bamount, btax, btamount);
                final_result[CGST2_5] = final_result[CGST2_5].add(bcgst2_5);
                final_result[CGST6] = final_result[CGST6].add(bcgst6);
                final_result[CGST9] = final_result[CGST9].add(bcgst9);
                final_result[CGST14] = final_result[CGST14].add(bcgst14);
                final_result[SGST2_5] = final_result[SGST2_5].add(bsgst2_5);
                final_result[SGST6] = final_result[SGST6].add(bsgst6);
                final_result[SGST9] = final_result[SGST9].add(bsgst9);
                final_result[SGST14] = final_result[SGST14].add(bsgst14);
                final_result[IGST5] = final_result[IGST5].add(bigst5);
                final_result[IGST12] = final_result[IGST12].add(bigst12);
                final_result[IGST18] = final_result[IGST18].add(bigst18);
                final_result[IGST28] = final_result[IGST28].add(bigst28);

                final_result[AMOUNT] = final_result[AMOUNT].add(bamount);

                final_result[TAX] = final_result[TAX].add(btax);
                final_result[TAMOUNT] = final_result[TAMOUNT].add(btamount);

            }
//            if (s_no % 15 < 15) {
//                int min_height = (16 - s_no % 15) * 10;
//                Cell fill = new Cell(1, 20)
//                        .add("")
//                        .setFontSize(9)
//                        .setBorder(Border.NO_BORDER)
//                        .setBackgroundColor(c).setMinHeight(min_height);
//                sales_table.addCell(fill);
//            }
            add_total();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales_table;
    }

    private void fill_table(int s_no, int bill_no, String party_name,
            String town, String stin,
            String cgst2_5, String cgst6, String cgst9, String cgst14,
            String sgst2_5, String sgst6, String sgst9, String sgst14,
            String igst5, String igst12, String igst18, String igst28,
            BigDecimal amount, BigDecimal ttax, BigDecimal total
    ) {
        Color c = Color.WHITE;
        Cell csno = new Cell(1, 1)
                .add(String.valueOf(s_no))
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c);
        Cell cbn = new Cell(1, 1)
                .add(String.valueOf(bill_no))
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpn = new Cell(1, 1)
                .add(party_name)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpt = new Cell(1, 1)
                .add(town)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);
        Cell tin = new Cell(1, 1)
                .add(stin)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);

        sales_table.addCell(csno)
                .addCell(cbn)
                .addCell(cpn)
                .addCell(cpt)
                .addCell(tin)
                .addCell(new Cell(1, 1)
                        .add(cgst2_5)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(cgst6)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(cgst9)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(cgst14)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(sgst2_5)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(sgst6)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(sgst9)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(sgst14)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(igst5)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(igst12)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(igst18)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(igst28)
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(amount))
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(ttax))
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(total))
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c));

    }

    private void add_total() {
        Color c = Color.BLUE;
        Cell csno = new Cell(1, 1)
                .add("Total")
                .setFontSize(9)
                .setBorderRight(Border.NO_BORDER)
                .setBackgroundColor(c);
        Cell cbn = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpn = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpt = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
        Cell tin = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);

        sales_table.addCell(csno)
                .addCell(cbn)
                .addCell(cpn)
                .addCell(cpt)
                .addCell(tin)
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[CGST2_5]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[CGST6]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[CGST9]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[CGST14]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[SGST2_5]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[SGST6]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[SGST9]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[SGST14]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[IGST5]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[IGST12]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[IGST18]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(final_result[IGST28]))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(String.valueOf(final_result[AMOUNT])))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(String.valueOf(final_result[TAX])))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(String.valueOf(final_result[TAMOUNT])))
                        .setFontSize(9)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c));

    }

    void lr_report(LocalDate d1, LocalDate d2) {

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("HH_mm_ss");
        String currentTime = sdf.format(dt);
        String file_no = new String("1");
        String data;

        if (addLrRepItems() == null) {
            printNoDataPdf(d1, d2);
            return;
        }
        for (int i = 0; i < 1; i++) {

            String dest = Utils.getRepDir() + "LR_Report_" + d1 + " -to-"
                    + d2 + "_" + currentTime
                    + "_" + file_no + ".pdf";

            try {
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

                //For page header
                VariableHeaderEventHandler handler = new VariableHeaderEventHandler(
                        true, false, null, 3);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

                Document doc = new Document(pdfDoc, PageSize.A4.rotate());
                doc.setMargins(60, 20, 20, 20);

                //For page header
                handler.setHeader(Utils.getCompanyName() + String.format(
                        " LR Report \n (From: %s To: %s)\n", d1, d2));

                doc.add(lr_table);

                doc.close();
                doc = null;
                pdfDoc = null;
                handler = null;
                file_no = String.valueOf(Integer.parseInt(file_no) + 1);
            } catch (Exception e) {
                Alert error41 = new Alert(Alert.AlertType.INFORMATION);
                error41.setContentText("lr report " + e.getMessage());
                e.printStackTrace(System.out);
                error41.showAndWait();
            }

        }
    }

    private Table addLrRepItems() {
        Color bg = new DeviceRgb(244, 244, 244);
        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9;
        cell1 = cell2 = cell3 = cell4 = cell5
                = cell6 = cell7 = cell8 = cell9 = new Cell(1, 1);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            cell1 = new Cell(1, 1)
                    .add(new Paragraph("SNO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell2 = new Cell(1, 1)
                    .add(new Paragraph("INV NO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell3 = new Cell(1, 1)
                    .add(new Paragraph("Date"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell4 = new Cell(1, 1)
                    .add(new Paragraph("Dealer Name"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell5 = new Cell(1, 1)
                    .add(new Paragraph("Address"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell6 = new Cell(1, 1)
                    .add(new Paragraph("Booking To"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell7 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("L R NO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell8 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Cases"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell9 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Transport"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            float[] columnWidths = {3, 3, 10, 35, 100, 30, 10, 3, 20};
            lr_table = new Table(columnWidths);
            lr_table.setWidthPercent(100);

            lr_table.addHeaderCell(cell1)
                    .addHeaderCell(cell2)
                    .addHeaderCell(cell3)
                    .addHeaderCell(cell4)
                    .addHeaderCell(cell5)
                    .addHeaderCell(cell6)
                    .addHeaderCell(cell7)
                    .addHeaderCell(cell8)
                    .addHeaderCell(cell9);
            fill_lr_items(lr_table);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return lr_table;
    }

    private void fill_lr_items(Table t) {
        Color c = Color.WHITE;
        try {
            String str = "SELECT * FROM transport t "
                    + "INNER JOIN ADDRESS a ON t.bt = a.id"
                    + " HAVING Date(date_time)  BETWEEN ? AND ? ";

            PreparedStatement p_stmt = Utils.getConnection().prepareStatement(str);
            p_stmt.setDate(1, java.sql.Date.valueOf(day1.getValue()));
            p_stmt.setDate(2, java.sql.Date.valueOf(day2.getValue()));

            ResultSet rs = p_stmt.executeQuery();

            int s_no = 1;
            while (rs.next()) {
                Cell csno = new Cell(1, 1)
                        .add(String.valueOf(s_no++))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell cinvno = new Cell(1, 1)
                        .add(String.valueOf(rs.getInt("t.bill_no")))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell cdate = new Cell(1, 1)
                        .add(rs.getDate("t.date_time").toString())
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell cname = new Cell(1, 1)
                        .add(rs.getString("a.party_name"))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell caddr = new Cell(1, 1)
                        .add(rs.getString("a.addr") + " " + rs.getString("a.addr1"))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell cbk = new Cell(1, 1)
                        .add(rs.getString("a.bk"))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell clr = new Cell(1, 1)
                        .add(rs.getString("t.lrno"))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell cc = new Cell(1, 1)
                        .add(rs.getString("t.cases"))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);
                Cell ct = new Cell(1, 1)
                        .add(rs.getString("t.name"))
                        .setFontSize(9)
                        .setBackgroundColor(c)
                        .setTextAlignment(TextAlignment.LEFT);

                t.addCell(csno)
                        .addCell(cinvno)
                        .addCell(cdate)
                        .addCell(cname)
                        .addCell(caddr)
                        .addCell(cbk)
                        .addCell(clr)
                        .addCell(cc)
                        .addCell(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void sales_report_red(LocalDate d1, LocalDate d2) {

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("HH_mm_ss");
        String currentTime = sdf.format(dt);
        String file_no = new String("1");
        String data;

        if (addSalesRepRedItems() == null) {
            printNoDataPdf(d1, d2);
            return;
        }
        for (int i = 0; i < 1; i++) {

            String dest = Utils.getRepDir() + "Sales_Report_Sample" + d1 + " -to-"
                    + d2 + "_" + currentTime
                    + "_" + file_no + ".pdf";

            try {
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

                //For page header
                VariableHeaderEventHandler handler = new VariableHeaderEventHandler(
                        true, false, null, 3);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

                Document doc = new Document(pdfDoc, PageSize.A4.rotate());
                doc.setMargins(60, 20, 20, 20);

                //For page header
                handler.setHeader(Utils.getCompanyName() + String.format(
                        " Sales Report \n (From: %s To: %s)\n", d1, d2));

                doc.add(sales_table_red);

                doc.close();
                doc = null;
                pdfDoc = null;
                handler = null;
                file_no = String.valueOf(Integer.parseInt(file_no) + 1);
            } catch (Exception e) {
                Alert error41 = new Alert(Alert.AlertType.INFORMATION);
                error41.setContentText("callIText Red " + e.getMessage());
                e.printStackTrace(System.out);
                error41.showAndWait();
            }

        }
    }

    private Table addSalesRepRedItems() {
        Color bg = new DeviceRgb(244, 244, 244);
        Cell cell1, cell5, cell3, cell4, cell6,
                cell9, cell12, cell13;
        cell1 = cell3 = cell5 = cell4 = cell6
                = cell9 = cell12 = cell13 = new Cell(1, 1);

        float[] col = {4f, 4f, 15f, 15f, 14f,
            10f, 10f, 10f};

        sales_table_red = new Table(col);
        sales_table_red.setWidthPercent(100);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            cell1 = new Cell(1, 1)
                    // .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SNO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell5 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Bill No."))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell3 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Custormer Name"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell4 = new Cell(1, 1)
                    //    .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Town"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell6 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("GSTIN"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell9 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //  .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Tax"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell12 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph(" Taxable \n Amount"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell13 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph(" Total Amount"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            sales_table_red.addHeaderCell(cell1)
                    .addHeaderCell(cell5)
                    .addHeaderCell(cell3)
                    .addHeaderCell(cell4)
                    .addHeaderCell(cell6)
                    .addHeaderCell(cell12)
                    .addHeaderCell(cell9)
                    .addHeaderCell(cell13);

        } catch (IOException e) {
            e.printStackTrace();
        }

        int no_of_items = 0;
        try {
            String str0 = "SELECT COUNT(DISTINCT bill_no) as cnt"
                    + " FROM bills b WHERE  Date(b.date_time)"
                    + " BETWEEN ? AND ? AND"
                    + " b.cancel <> '1' AND b.deleted <> '1'"
                    + " ";
            PreparedStatement p0 = Utils.getConnection().prepareStatement(str0);
            p0.setDate(1, java.sql.Date.valueOf(day1.getValue()));
            p0.setDate(2, java.sql.Date.valueOf(day2.getValue()));
            ResultSet rs0 = p0.executeQuery();

            if (rs0.next()) {
                no_of_items = rs0.getInt("cnt");
            }
            String str1 = "SELECT *  FROM bills b INNER JOIN "
                    + " ADDRESS a ON a.id=b.phone_num AND  Date(b.date_time)"
                    + " BETWEEN ? AND ? AND"
                    + " b.cancel <> '1' AND b.deleted <> '1'"
                    + " ORDER BY b.bill_no ASC";
            PreparedStatement p1 = Utils.getConnection().prepareStatement(str1);
            p1.setDate(1, java.sql.Date.valueOf(day1.getValue()));
            p1.setDate(2, java.sql.Date.valueOf(day2.getValue()));
            ResultSet rs1 = p1.executeQuery();
            int s_no = 0;
            Color c = Color.WHITE;

            //  ArrayList<ArrayList<BigDecimal>> values = new ArrayList<>();
            //    HashMap<Integer, Integer> hm = new HashMap();
            //     HashMap<Integer, String[]> details = new HashMap();
            int curr_item = 0;
            BigDecimal bamount, btax, btamount,
                    iamount, itax;
            bamount = btax = btamount = iamount = itax = BigDecimal.ZERO;
            String[] info = new String[3];
            boolean first_time = true;
            boolean ot = true;
            BigDecimal bbtax = BigDecimal.ZERO;
            Arrays.fill(final_result_red, BigDecimal.ZERO);

            int old_bill_no = 0;
            while (rs1.next()) {
                if (curr_item != rs1.getInt("bill_no")) {

                    if (!first_time) {

                        fill_table_red(s_no, curr_item, info[0], info[1], info[2],
                                bamount, btax, btamount);

                    } else {
                        first_time = false;

                    }
                    final_result_red[RED_AMOUNT] = final_result_red[RED_AMOUNT].add(bamount);

                    final_result_red[RED_TAX] = final_result_red[RED_TAX].add(btax);
                    final_result_red[RED_TAMOUNT] = final_result_red[RED_TAMOUNT].add(btamount);

                    curr_item = rs1.getInt("bill_no");
                    s_no++;

                    info[0] = new String(rs1.getString("party_name"));
                    info[1] = new String(rs1.getString("town"));
                    info[2] = new String(rs1.getString("tin"));
                    btax = BigDecimal.valueOf(rs1.getDouble("tax"));

                    bamount = btamount = iamount = BigDecimal.ZERO;
                    ot = true;
                }
                iamount = BigDecimal.valueOf(rs1.getDouble("amount"));
                bamount = bamount.add(iamount);
                btamount = btamount.add(iamount);
                if (ot == true) {
                    ot = false;
                    btamount = iamount.add(btax);
                }
            }
            if (s_no != 0) {
                fill_table_red(s_no, curr_item, info[0], info[1], info[2],
                        bamount, btax, btamount);
                final_result_red[RED_AMOUNT] = final_result_red[RED_AMOUNT].add(bamount);

                final_result_red[RED_TAX] = final_result_red[RED_TAX].add(btax);
                final_result_red[RED_TAMOUNT] = final_result_red[RED_TAMOUNT].add(btamount);

            }
            add_total_red();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales_table_red;
    }

    private void fill_table_red(int s_no, int bill_no, String party_name,
            String town, String stin,
            BigDecimal amount, BigDecimal ttax, BigDecimal total
    ) {
        Color c = Color.WHITE;
        Cell csno = new Cell(1, 1)
                .add(String.valueOf(s_no))
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c);
        Cell cbn = new Cell(1, 1)
                .add(String.valueOf(bill_no))
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpn = new Cell(1, 1)
                .add(party_name)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpt = new Cell(1, 1)
                .add(town)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);
        Cell tin = new Cell(1, 1)
                .add(stin)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(c)
                .setTextAlignment(TextAlignment.LEFT);

        sales_table_red.addCell(csno)
                .addCell(cbn)
                .addCell(cpn)
                .addCell(cpt)
                .addCell(tin)
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(amount))
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(ttax))
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(total))
                        .setFontSize(9)
                        .setBorder(Border.NO_BORDER)
                        .setBackgroundColor(c));

    }

    private void add_total_red() {
        Color c = Color.BLUE;
        Cell csno = new Cell(1, 1)
                .add("Total")
                .setFontSize(9)
                .setBorderRight(Border.NO_BORDER)
                .setBackgroundColor(c);
        Cell cbn = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpn = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
        Cell cpt = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
        Cell tin = new Cell(1, 1)
                .add("--")
                .setFontSize(9)
                .setBackgroundColor(c)
                .setBorderRight(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);

        sales_table_red.addCell(csno)
                .addCell(cbn)
                .addCell(cpn)
                .addCell(cpt)
                .addCell(tin)
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(String.valueOf(final_result_red[RED_AMOUNT])))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(String.valueOf(final_result_red[RED_TAX])))
                        .setFontSize(9)
                        .setBorderRight(Border.NO_BORDER)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c))
                .addCell(new Cell(1, 1)
                        .add(String.valueOf(String.valueOf(final_result_red[RED_TAMOUNT])))
                        .setFontSize(9)
                        .setBorderLeft(Border.NO_BORDER)
                        .setBackgroundColor(c));

    }

}
