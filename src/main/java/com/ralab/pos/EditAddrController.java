/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import static com.ralab.pos.AddressBookController.addAddressesFromDB;
import static com.ralab.pos.AddressBookController.addr_data;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class EditAddrController implements Initializable {

    @FXML
    private TextField town;
    @FXML
    private TextField pname;
    @FXML
    private TextField addr1;
    @FXML
    private TextField addr2;
    @FXML
    private TextField cell;
    @FXML
    private TextField gstin;
    @FXML
    private TextField climit;
    @FXML
    private TextField pdc;
    @FXML
    private TextField bk;
    @FXML
    private TextField tpt;
    static StringProperty str_prop_addr_id;
    
    static int global_addr_id = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        str_prop_addr_id = new SimpleStringProperty(this, "str_prop_addr_id", "");
        str_prop_addr_id.addListener((obs, oldValue, newValue)
                -> {fillEditAddr(newValue);  });
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String tow = town.getText();
        String party_nam = pname.getText();
        String add1 = addr1.getText();
        String add2 = addr2.getText();
        String cel = cell.getText();
        String ti = gstin.getText();
        String climi = climit.getText();
        String pd = pdc.getText();
        String b = bk.getText();
        String tp = tpt.getText();

        if (tow.length() == 0
                || party_nam.length() == 0
                || add1.length() == 0) {
            return;
        }
        try {
            String str = "UPDATE  ADDRESS "
                    + " SET town = ?, party_name = ?, addr = ?, addr1 = ?, cell = ?, tin= ?, climit= ?,"
                    + "pdc = ?, bk = ?, tpt = ?  WHERE id=?";
            PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(str);

            p_stmt.setString(1, tow);
            p_stmt.setString(2, party_nam);
            p_stmt.setString(3, add1);
            p_stmt.setString(4, add2);
            p_stmt.setString(5, cel);
            p_stmt.setString(6, ti);
            p_stmt.setString(7, climi);
            p_stmt.setString(8, pd);
            p_stmt.setString(9, b);
            p_stmt.setString(10, tp);
            p_stmt.setInt(11, global_addr_id);

            p_stmt.executeUpdate();

            clearAddrArea();
            global_addr_id = 0;
            addr_data.clear();
             addAddressesFromDB(addr_data);
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void setAddrId(String addrId) {
        str_prop_addr_id.set(addrId);
    }

    private void fillEditAddr(String strId) {
        global_addr_id = Integer.parseInt(strId);
        try {
            String str = "SELECT * FROM ADDRESS WHERE id=?";
            PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(str);
            p_stmt.setInt(1, Integer.parseInt(strId));
            ResultSet rs = p_stmt.executeQuery();
            if (rs.next()) {

                town.setText(rs.getString("town"));
                pname.setText(rs.getString("party_name"));
                addr1.setText(rs.getString("addr"));
                addr2.setText(rs.getString("addr1"));
                cell.setText(rs.getString("cell"));
                gstin.setText(rs.getString("tin"));
                climit.setText(rs.getString("climit"));
                pdc.setText(rs.getString("pdc"));
                bk.setText(rs.getString("bk"));
                tpt.setText(rs.getString("tpt"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void clearAddrArea(){
        town.clear();
        pname.clear();
        addr1.clear();
        addr2.clear();
        cell.clear();
        gstin.clear();
        climit.clear();
        pdc.clear();
        bk.clear();
        tpt.clear();
    }
}
