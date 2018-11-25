/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import static com.ralab.pos.EditAddrController.setAddrId;
import static com.ralab.pos.EditAddrController.global_addr_id;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class AddressBookController implements Initializable {

    @FXML
    private TableView<Address> addr_book1;
    @FXML
    private TextField town;
    @FXML
    private TextField pname;
    @FXML
    private TextField addr1;
    @FXML
    private TextField addr2;
    @FXML
    private TextField phone;
    @FXML
    private TextField tin;
    @FXML
    private TextField climit;
    @FXML
    private TextField pdc;
    @FXML
    private TextField bk;
    @FXML
    private TextField tpt;

    static boolean editaddr_stage_on = false;

    static Stage editaddr_stage;
    /**
     * Initializes the controller class.
     */
    /* Item Array */
    static ObservableList<Address> addr_data = FXCollections.observableArrayList( //            new BillItem( "1", "Tyre" , "Smih", "jom", "12", "pcs", "1240", "100")
            );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addr_book1.setItems(addr_data);

        addAddressesFromDB(addr_data);
    }

    @FXML
    private void handleAddPartyButtonAction(ActionEvent event) {
        String tow = town.getText();
        String party_nam = pname.getText();
        String add1 = addr1.getText();
        String add2 = addr2.getText();
        String cel = phone.getText();
        String ti = tin.getText();
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
            String str = "INSERT INTO ADDRESS "
                    + "(town, party_name, addr, addr1, cell, tin, climit,"
                    + "pdc, bk, tpt) VALUES(?,?,?,?,?,?,?,?,?,?)";
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

            p_stmt.executeUpdate();

            clearAddArea();
            addr_data.clear();
            addAddressesFromDB(addr_data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAddressesFromDB(ObservableList<Address> data) {

        try {
            String str = "SELECT * FROM ADDRESS";
            PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(str);
            ResultSet rs = p_stmt.executeQuery();
            while (rs.next()) {
                Address a = new Address(
                        rs.getInt("id"),
                        rs.getString("town"),
                        rs.getString("party_name"),
                        rs.getString("addr"),
                        rs.getString("addr1"),
                        rs.getString("cell"),
                        rs.getString("tin"),
                        rs.getString("climit"),
                        rs.getString("pdc"),
                        rs.getString("bk"),
                        rs.getString("tpt"),
                        ""
                );

                data.add(a);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearAddArea() {
        town.clear();
        pname.clear();
        addr1.clear();
        addr2.clear();
        phone.clear();
        tin.clear();
        climit.clear();
        pdc.clear();
        bk.clear();
        tpt.clear();

    }

    @FXML
    private void handleRowSelect() {
        Address a = addr_book1.getSelectionModel().getSelectedItem();
        if (a.getSNo() == 9999) {
            //send a WARNING message that you cannot edit this
        } else {
            if (!editaddr_stage_on) {
                showEditAddr();
                editaddr_stage_on = true;
                setAddrId(String.valueOf(a.getSNo()));
            } else {
                editaddr_stage.toFront();
            }
        }
    }

    private void showEditAddr() {

        editaddr_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/EditAddr.fxml"));
            Scene scene = new Scene(root, 600, 600);

            editaddr_stage.setTitle(Utils.getCompanyName() + " Software");
            editaddr_stage.setScene(scene);
            editaddr_stage.setResizable(false);
            editaddr_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showAddProd " + e.getMessage());
            error.showAndWait();
        }
        editaddr_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                editaddr_stage_on = false;
               global_addr_id = 0;
            }

        });

    }

}
