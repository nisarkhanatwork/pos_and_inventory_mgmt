/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class PurchaseController implements Initializable {

    @FXML
    private ComboBox<String> pur_mfg;
    @FXML
    private ComboBox<String> pur_prod;
    @FXML
    private ComboBox<String> pur_units;
    @FXML
    private TextField pur_price;
    @FXML
    private TextField pur_quant;
    @FXML
    private TextField pur_low;
    @FXML
    private TextField pur_prod_code;

    ObservableList<String> mfg_data = FXCollections.observableArrayList();
    ObservableList<String> prod_data = FXCollections.observableArrayList();
    ObservableList<String> units_data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        pur_mfg.setItems(mfg_data);
        pur_prod.setItems(prod_data);
        pur_units.setItems(units_data);

        fillMfg(mfg_data);
        pur_price.setEditable(false);
        pur_low.setEditable(false);
        pur_prod_code.setEditable(false);
    }

    @FXML
    private void handleMfgComboBoxAction(ActionEvent event) {
        if (pur_mfg.getValue() != null) {
            prod_data.clear();
            setProdFromDB(pur_mfg.getValue());
        }

    }

    @FXML
    private void handleProdComboBoxAction(ActionEvent event) {
        if (pur_prod.getValue() != null) {
            setProdID(pur_prod.getValue().toString(),
                    pur_mfg.getValue().toString());
        }
    }

    @FXML
    private void handleUnitsComboBoxAction(ActionEvent event) {
    }

    @FXML
    private void handleAddItemButtonAction(ActionEvent event) {
        String q = pur_quant.getText();

        if (!(q.length() == 0
                || Validator.notValidAmount(q))) {
            String s = pur_prod_code.getText();

            try {
                Statement stmt = Utils.getConnection().createStatement();
                String pur_str = "INSERT INTO purchase (prod_code, quant)"
                        + "VALUES (?, ?)";
                PreparedStatement pAddPur = Utils.getConnection().prepareStatement(pur_str);
                pAddPur.setString(1, s);
                pAddPur.setDouble(2, Double.parseDouble(q));
                pAddPur.executeUpdate();
                clearPurchase();

            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("handleAddItemCButton " + e.getMessage());
                error.showAndWait();

            }
        }
    }

    private void fillMfg(ObservableList<String> mfg_data) {
        try {
            Statement stmt = Utils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT mfg FROM"
                    + " products ORDER BY mfg ASC");

            while (rs.next()) {
                String s = rs.getString("mfg");
                mfg_data.add(s);
            }

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("fillMfg " + e.getMessage());
            error.showAndWait();

        }
    }

    public void setProdFromDB(String m) {
        try {
            Statement stmt = Utils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT prod FROM products WHERE "
                    + " mfg =\"" + m
                    + "\" ORDER by prod ASC");

            while (rs.next()) {
                String r = rs.getString("prod");
                prod_data.add(r);
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("setProdFromDB " + e.getMessage());
            error.showAndWait();
        }

    }

    public void setProdID(String p, String m) {

        try {
            Statement stmt = Utils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE "
                    + " mfg =\"" + m + "\" "
                    + "AND prod =\"" + p + "\"");

            while (rs.next()) {
                String pc = rs.getString("prod_code");
                String u = rs.getString("units");
                String r = rs.getString("price");
                String l = rs.getString("low_warn");

                pur_prod_code.setText(pc);
                pur_low.setText(l);
                units_data.clear();
                units_data.add(u);
                pur_price.setText(r);
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("setProdID " + e.getMessage());
            error.showAndWait();
        }
    }

    private void clearPurchase() {
        mfg_data.clear();
        prod_data.clear();
        units_data.clear();
        pur_prod_code.clear();
        pur_quant.clear();
        pur_price.clear();

        pur_mfg.setValue(null);
        pur_prod.setValue(null);
        pur_units.setValue(null);
    }
}
