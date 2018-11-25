/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class LoadDataController implements Initializable {

    @FXML
    private Button choose_file;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void handleChooseFilesAction(ActionEvent event) {
        if (Utils.isRedBilling()) {
            final FileChooser fileChooser = new FileChooser();
            configureFileChooser(fileChooser);
            List<File> list
                    = fileChooser.showOpenMultipleDialog(choose_file.getScene().getWindow());
            if (list != null) {
                for (File file : list) {
                    insertSalesIntoDB(file);
                }
            }
        } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("ERROR!");
            error.showAndWait();
        }

    }

    private void insertSalesIntoDB(File bill_file) {

        Bill bill = new Bill();
        SerialBill sbill = new SerialBill();

        Gson gson = new GsonBuilder().create();
        try {
            Reader reader = new FileReader(bill_file);
            sbill = gson.fromJson(reader, SerialBill.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bill.bill_no = sbill.bill_no;
        bill.btCustomer = sbill.btCustomer;
        bill.date_time = sbill.date_time;
        bill.quote_no = sbill.quote_no;
        bill.tax = sbill.tax;
        bill.tr = new Transport(sbill.tr);
        sbill.getItems().forEach(bi -> {
            bill.addItem(bi);
        });
        int cid = bill.btCustomer.getSNo();
        int sid = bill.stCustomer.getSNo();
        boolean is_gst = Utils.isGSTEnabled();

        bill.getItems().forEach(bi -> {
            try {
                if (!(bi.getSerialNo().equals("Total")
                        || bi.getSerialNo().equals("Sub Total")
                        || bi.getSerialNo().equals("Tax"))) {
                    String str1 = "INSERT INTO bills (date_time, "
                            + "s_no, bill_no, prod_code, quant, amount, rate, phone_num,"
                            + " phone_num1, is_gst, is_igst, gst, tax) "
                            + "VALUES ( ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt1 = Utils.getConnection().prepareStatement(str1);
                    stmt1.setString(1, bill.date_time);
                    stmt1.setInt(2, Integer.parseInt(bi.getSerialNo()));
                    stmt1.setInt(3, Integer.parseInt(bill.bill_no));
                    stmt1.setString(4, bi.getProdCode());
                    stmt1.setDouble(5, Double.parseDouble(bi.getQuantValue()));
                    stmt1.setDouble(6, Double.parseDouble(bi.getAmountValue()));
                    stmt1.setDouble(7, Double.parseDouble(bi.getRateValue()));
                    stmt1.setString(8, Integer.toString(cid));
                    stmt1.setString(9, Integer.toString(sid));
                    stmt1.setBoolean(10, is_gst);
                    stmt1.setBoolean(11, false); //about igst
                    stmt1.setDouble(12, Double.parseDouble(bi.getGst()));
                    stmt1.setDouble(13, Double.parseDouble(bi.getRtax()));

                    stmt1.executeUpdate();
                }

            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("LoadDataController->insertSalesIntoDB " + e.getMessage());
                error.showAndWait();
                e.printStackTrace();
            }
        });

        try {
            String tr_str = "INSERT INTO transport (bill_no, name,"
                    + "lrno, lrdate, weight, cases, pvtmark, bk_to, bt)"
                    + "VALUES (?,?,?,?,?,?,?,?, ?)";
            PreparedStatement tr_stmt = Utils.getConnection().prepareCall(tr_str);
            tr_stmt.setInt(1, Integer.parseInt(bill.bill_no));
            tr_stmt.setString(2, bill.tr.getTrName());
            tr_stmt.setString(3, bill.tr.getLrNo());
            tr_stmt.setString(4, bill.tr.getLrDate());
            tr_stmt.setString(5, bill.tr.getWeight());
            tr_stmt.setString(6, bill.tr.getCases());
            tr_stmt.setString(7, bill.tr.getPvtMark());
            tr_stmt.setString(8, bill.tr.getBkTo());
            tr_stmt.setInt(9, bill.btCustomer.getSNo());
            tr_stmt.executeUpdate();
            bill.clearTransport();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Load data");
        fileChooser.setInitialDirectory(
                new File(Utils.getDbBkpDir())
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All bdat files", "*.bdat")
        );
    }
}
