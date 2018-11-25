/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author lab149
 */
public class Bill {

    Transport tr;
    Address btCustomer;
    Address stCustomer;
    String bill_no;
    String quote_no;
    String date_time;
    String tax;
    
    private ObservableList<BillItem> items = FXCollections.observableArrayList();

    Bill() {
        tr = new Transport("", "", "", "", "", "", "");
        btCustomer = new Address();
        stCustomer = new Address();
        bill_no = new String();
        quote_no = new String();
    }

    public void clearTransport() {
        tr.setTrName("");
        tr.setLrNo("");
        tr.setLrDate("");
        tr.setBkTo("");
        tr.setWeight("");
        tr.setCases("");
        tr.setPvtMark("");

    }

    public void addItem(int i, BillItem bi) {
        items.add(i, bi);
    }

    public void addItem(BillItem bi) {
        items.add(bi);
    }

    public void delItem(int i) {
        items.remove(i);
    }

    public BillItem getItem(int i) {
        return items.get(i);
    }

    public void clear() {
        items.clear();
    }

    public int getSize() {
        return items.size();
    }

    public void setBillNo(String b) {
        bill_no = b;
    }

    public void setQuoteNo(String q) {
        quote_no = q;
    }
    public void setTime(String t){
        date_time = t;
    }
    public String getBillNo() {

        String data_from_db = new String();
        boolean found = false;
        try {
            String p_str = "SELECT no FROM bill_no ";
            PreparedStatement p_stmt = Utils.getConnection().prepareStatement(p_str);
            ResultSet rs = p_stmt.executeQuery();

            while (rs.next()) {
                data_from_db = rs.getString("no");
                found = true;

            }
            if (!found) {
                data_from_db = "1";
                updateBillNoInDB("", true);
            }

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("getBillNo " + e.getMessage());
            error.showAndWait();

        }

        bill_no = data_from_db;
        return data_from_db;
    }

    public String getQuoteNo() {

        String data_from_db = new String();
        boolean found = false;
        try {
            String p_str = "SELECT quote_no FROM bill_no ";
            PreparedStatement p_stmt = Utils.getConnection().prepareStatement(p_str);
            ResultSet rs = p_stmt.executeQuery();

            while (rs.next()) {
                data_from_db = rs.getString("quote_no");
                found = true;
            }
            if (!found) {
                data_from_db = "1";
                updateQuoteNoInDB("", true);
            }

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("getQuoteNo " + e.getMessage());
            error.showAndWait();

        }
        quote_no = data_from_db;
        return data_from_db;
    }

    public static void updateBillNoInDB(String data_to_db, Boolean b) {
        if (b) {
            try {
                String str = "SELECT * FROM bill_no";
                PreparedStatement p = Utils.getConnection().prepareStatement(str);
                ResultSet rs = p.executeQuery();
                if (!rs.next()) {

                    String p_str = "INSERT INTO bill_no (id, no, quote_no) "
                            + "VALUES (?, ?, ?)";
                    PreparedStatement p_stmt = Utils.getConnection().prepareStatement(p_str);
//                    p_stmt.setString(1, currentDate);
                    p_stmt.setInt(1, 1);
                    p_stmt.setInt(2, 1);
                    p_stmt.setInt(3, 1);
                    p_stmt.executeUpdate();
                }
            } catch (SQLException e) {
                Alert print_error = new Alert(Alert.AlertType.ERROR);
                print_error.setContentText("updateBillNoInDB 1" + e.getMessage());
                print_error.showAndWait();

            }
        } else {
            try {
                String str = "UPDATE bill_no SET no= ? ";
                PreparedStatement stmt = Utils.getConnection().prepareStatement(str);
                stmt.setInt(1, Integer.parseInt(data_to_db));
                stmt.executeUpdate();

            } catch (SQLException e) {
                Alert print_error = new Alert(Alert.AlertType.ERROR);
                print_error.setContentText("updateBillNoInDB 2" + e.getMessage());
                print_error.showAndWait();

            }

        }
    }

    public static void updateQuoteNoInDB(String data_to_db, Boolean b) {
        if (b) {
            try {
                String str1 = "INSERT INTO bill_no (quote_no) "
                        + "VALUES ( ?)";
                PreparedStatement p_stmt1 = Utils.getConnection().prepareStatement(str1);
                p_stmt1.setInt(1, 1);
                int ret = p_stmt1.executeUpdate();
            } catch (SQLException e) {
                Alert print_error = new Alert(Alert.AlertType.ERROR);
                print_error.setContentText("updateQuoteNoInDB 1" + e.getMessage());
                print_error.showAndWait();

            }
        } else {
            try {

                String str2 = "UPDATE bill_no SET quote_no= ? ";
                PreparedStatement p_stmt2 = Utils.getConnection().prepareStatement(str2);
                p_stmt2.setInt(1, Integer.parseInt(data_to_db));
                int ret = p_stmt2.executeUpdate();
            } catch (SQLException e) {
                Alert print_error = new Alert(Alert.AlertType.ERROR);
                print_error.setContentText("updateQuoteNoInDB 2" + e.getMessage());
                print_error.showAndWait();

            }

        }
    }

    public ObservableList<BillItem> getItems() {
        return items;
    }

    

}
