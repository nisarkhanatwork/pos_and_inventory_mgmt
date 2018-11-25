package com.ralab.pos;

import java.math.BigDecimal;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lab149
 */
public class AddProductController implements Initializable{

//    private TextField creat_mfg_txt;
//    private ComboBox<String> creat_mfg_cmb;
//    private TextField creat_prod_txt;
//    private ComboBox<String> creat_prod_cmb;
//    private TextField creat_units_txt;
//    private ComboBox<String> creat_units_cmb;
//    private TextField creat_price_txt;
//    private TextField creat_low_warn_txt;
//    private TextField creat_prod_code_txt;
//    private TextField creat_gst_txt;
//    private TextField creat_hsn_code_txt;

    
    @FXML
    private Button creat_mfg_btn;
    @FXML
    private TextField creat_mfg_txt;
    @FXML
    private ComboBox<String> creat_mfg_cmb;
    @FXML
    private Button creat_prod_btn;
    @FXML
    private TextField creat_prod_txt;
    @FXML
    private ComboBox<String> creat_prod_cmb;
    @FXML
    private Button creat_units_btn;
    @FXML
    private TextField creat_units_txt;
    @FXML
    private ComboBox<String> creat_units_cmb;
    @FXML
    private TextField creat_price_txt;
    @FXML
    private TextField creat_low_warn_txt;
    @FXML
    private TextField creat_prod_code_txt;
    @FXML
    private TextField creat_gst_txt;
    @FXML
    private TextField creat_hsn_code_txt;
    @FXML
    private Button creat_add_modify_btn11;
    @FXML
    private Button creat_del_btn;
    ObservableList<String> creat_mfg_data = FXCollections.observableArrayList();
    ObservableList<String> creat_prod_data = FXCollections.observableArrayList();
    ObservableList<String> creat_units_data = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        creat_mfg_cmb.setItems(creat_mfg_data);
        creat_prod_cmb.setItems(creat_prod_data);
        creat_units_cmb.setItems(creat_units_data);

        //servicing : clearing all products with prod_code == NULL
        //TODO: to enable it in production
        
        try {
            Statement stmt = Utils.getConnection().createStatement();
            stmt.executeUpdate("DELETE FROM products WHERE prod_code IS NULL");


        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("fillTypes " + e.getMessage());
            error.showAndWait();

        }
       
        fillMfg(creat_mfg_data);
    }
   @FXML 
    private void handleCreateMfgButtonAction(ActionEvent event) {
        String mfg = creat_mfg_txt.getText();
        if (mfg.length() == 0) {
            return;
        }
        try {
            Statement stmt1 = Utils.getConnection().createStatement();
            String creat_str = "INSERT INTO products (mfg)"
                    + "VALUES (?)";
            PreparedStatement ps = Utils.getConnection().prepareStatement(creat_str);
            ps.setString(1, creat_mfg_txt.getText());
            ps.executeUpdate();

            fillMfg(creat_mfg_data);
            creat_mfg_cmb.setValue(creat_mfg_txt.getText());
            creat_mfg_txt.clear();
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("handleCreateMfgButtonAction " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();

        }
    }
@FXML
    private void handleCreateMfgComboBoxAction(ActionEvent event) {
        if (creat_mfg_cmb.getValue() != null) {
            creat_prod_data.clear();
            creat_units_data.clear();
            fillProd(creat_prod_data, creat_mfg_cmb.getValue());
        }
    }
@FXML
    private void handleCreateProdButtonAction(ActionEvent event) {
        String s = creat_mfg_cmb.getValue();
        if (s == null
                || creat_prod_txt.getText().length() == 0) {
            return;
        }
//        try {
//            //Find if there is any product with mfg and prod, 
//            // if exists do nothing and return
//            Statement stmt_prod = Utils.getConnection().createStatement();
//            String prod_code_query = "SELECT prod_code FROM products "
//                    + "WHERE mfg = ? AND prod = ?";
//            PreparedStatement pc_query = Utils.getConnection().prepareStatement(
//                    prod_code_query);
//            pc_query.setString(2, creat_prod_txt.getText());
//            pc_query.setString(1, creat_mfg_cmb.getValue());
//            ResultSet rs1 = pc_query.executeQuery();
//            if (rs1.next()) {
//                if (rs1.getString("prod_code") != null) {
//                    Alert error = new Alert(Alert.AlertType.WARNING);
//                    error.setContentText("handleCreateProdButtonAction " + " prod already exists");
//                    error.showAndWait();
//
//                    fillProd(creat_prod_data, creat_mfg_cmb.getValue());
//                    creat_prod_cmb.setValue(creat_prod_txt.getText());
//
//                    creat_prod_txt.clear();
//                    return;
//                }
//            }

        try {
            //Create a product with the given mfg, and give its prod id 
            //as temp..later when prod and units details are given, give it a 
            //good prod id 
            //If empty prod does not exists already then create new prod, otherwise update
            Statement stmt_prod2 = Utils.getConnection().createStatement();
            String prod_code_query2 = "SELECT prod_code FROM products "
                    + "WHERE mfg = ? AND prod IS NULL";
            PreparedStatement pc_query2 = Utils.getConnection().prepareStatement(
                    prod_code_query2);
            //pc_query2.setNull(3, Types.NULL);

            pc_query2.setString(1, creat_mfg_cmb.getValue());
            ResultSet rs2 = pc_query2.executeQuery();

            if (rs2.next()) {
                Statement stmt1 = Utils.getConnection().createStatement();
                String update_str = "UPDATE  products SET prod=? WHERE mfg = ?"
                        + " AND prod IS NULL";
                PreparedStatement ps = Utils.getConnection().prepareStatement(update_str);
                ps.setString(1, creat_prod_txt.getText());

                ps.setString(2, creat_mfg_cmb.getValue());
                ps.executeUpdate();
                creat_prod_cmb.setValue(creat_prod_txt.getText());
                //creat_prod_txt.clear();
            } else {
                Statement stmt4 = Utils.getConnection().createStatement();
                String update_str4 = "INSERT INTO  products (prod, mfg) "
                        + "VALUES(?,?)";
                PreparedStatement ps4 = Utils.getConnection().prepareStatement(update_str4);
                ps4.setString(1, creat_prod_txt.getText());

                ps4.setString(2, creat_mfg_cmb.getValue());
                ps4.executeUpdate();

            }
            fillProd(creat_prod_data, creat_mfg_cmb.getValue());
            creat_prod_cmb.setValue(creat_prod_txt.getText());
            creat_prod_txt.clear();
            fillUnits(creat_units_data);
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("handleCreateProdButtonAction " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }

    }
@FXML
    private void handleCreateProdComboBoxAction(ActionEvent event) {
        fillUnits(creat_units_data);
        if (creat_prod_cmb.getValue() != null
                && creat_mfg_cmb.getValue() != null) {
            setProdParams(creat_prod_cmb.getValue().toString(),
                    creat_mfg_cmb.getValue().toString()
            );

        }
    }
@FXML
    private void handleCreateUnitsButtonAction(ActionEvent event) {

        if (creat_units_txt.getText().length() == 0
                || creat_prod_cmb.getValue() == null
                || creat_mfg_cmb.getValue() == null) {
            return;
        }
        try {

            Statement stmt1 = Utils.getConnection().createStatement();
            String update_str = "UPDATE  products SET units = ? WHERE "
                    + "  mfg = ? AND prod = ?";
            PreparedStatement ps = Utils.getConnection().prepareStatement(update_str);
            ps.setString(1, creat_units_txt.getText());

            ps.setString(2, creat_mfg_cmb.getValue());
            ps.setString(3, creat_prod_cmb.getValue());
            ps.executeUpdate();

            fillUnits(creat_units_data);

            creat_units_cmb.setValue(creat_units_txt.getText());
            creat_units_txt.clear();

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("handleCreateUnitsButtonAction " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();
            // clearProdAdd();

        }
    }

@FXML
    private void handleCreateAddModifyButtonAction(ActionEvent event) {
        String pri = creat_price_txt.getText();
        String lw = creat_low_warn_txt.getText();
        if (creat_prod_code_txt.getText().length() == 0
                || creat_prod_code_txt.getText().length() == 0
                || creat_mfg_cmb.getValue() == null
                || creat_prod_cmb.getValue() == null
                || creat_units_cmb.getValue() == null
                || Validator.notValidAmount(pri)
                || Validator.notValidAmount(lw)) {
            return;
        }
        try {

            //find if prod_code already exists..
            Statement stmt_prod = Utils.getConnection().createStatement();
            String prod_code_query = "SELECT  mfg, prod FROM products "
                    + "WHERE prod_code = ? ";
            PreparedStatement pc_query = Utils.getConnection().prepareStatement(
                    prod_code_query);
            pc_query.setString(1, creat_prod_code_txt.getText());

            ResultSet rs1 = pc_query.executeQuery();
            if (rs1.next()) {
                if (!rs1.getString("mfg").equals(creat_mfg_cmb.getValue())
                        || !rs1.getString("prod").equals(creat_prod_cmb.getValue())) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setContentText("prod code already exists");
                    error.showAndWait();

                    return;
                }
            }
            //update prod
            Statement stmt1 = Utils.getConnection().createStatement();
            String update_str = "UPDATE  products SET "
                    + "price = ?,"
                    + "low_warn = ?,"
                    + "prod_code = ?,"
                    + "hsn_code = ?,"
                    + "gst = ?,"
                    + "units = ?"
                    + "  WHERE mfg = ? AND prod = ?";
            PreparedStatement ps = Utils.getConnection().prepareStatement(update_str);

            String pc = creat_prod_code_txt.getText();
            String hsn = creat_hsn_code_txt.getText();
            double gst = Double.valueOf(creat_gst_txt.getText());
            String units = creat_units_cmb.getValue();

            ps.setDouble(1, Double.valueOf(pri));
            ps.setDouble(2, Double.valueOf(lw));
            ps.setString(3, pc);
            ps.setString(4, hsn);
            ps.setDouble(5, gst);
            ps.setString(6, units);

            ps.setString(7, creat_mfg_cmb.getValue());
            ps.setString(8, creat_prod_cmb.getValue());

            ps.executeUpdate();

            clearProdAddArea();
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("handleCreateAddModifyButtonAction " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();
            // clearProdAdd();

        }

    }
@FXML
    private void handleDELETEPRODButtonAction(ActionEvent event) {
        if (creat_mfg_cmb.getValue() == null
                || creat_prod_cmb.getValue() == null) {
            return;
        }

        try {

            Statement stmt = Utils.getConnection().createStatement();
            String prod_query = "DELETE  FROM products  "
                    + " WHERE  mfg = ? AND prod = ? ";
            PreparedStatement prod_ps = Utils.getConnection().prepareStatement(
                    prod_query);

            prod_ps.setString(1, creat_mfg_cmb.getValue());
            prod_ps.setString(2, creat_prod_cmb.getValue());

            prod_ps.executeUpdate();

            clearProdAddArea();

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("handleDELETEPRODButtonAction " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();

        }
    }

   @FXML
    private void handleCreateUnitsComboBoxAction(ActionEvent event) {
    }

    private void clearProdAddArea() {

        creat_mfg_txt.clear();
        creat_prod_txt.clear();
        creat_units_txt.clear();
        creat_price_txt.clear();

        creat_low_warn_txt.clear();
        creat_prod_code_txt.clear();
        creat_hsn_code_txt.clear();
        creat_gst_txt.clear();
       
        creat_mfg_data.clear();
        creat_prod_data.clear();
        creat_units_data.clear();

        creat_mfg_cmb.setValue(null);
        creat_prod_cmb.setValue(null);
        creat_units_cmb.setValue(null);
        fillMfg(creat_mfg_data);
        creat_mfg_cmb.setValue(null);

    }

    private void fillMfg(ObservableList<String> mfg_data) {
        mfg_data.clear();
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
            error.setContentText("AddProductController->fillMfg " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();

        }
    }

    private void fillProd(ObservableList<String> prod_data,
            String mfg) {
        prod_data.clear();
        try {
            Statement stmt = Utils.getConnection().createStatement();
            String ps_str = "SELECT DISTINCT prod FROM products "
                    + " WHERE mfg = ? ORDER BY prod ASC";
            PreparedStatement ps = Utils.getConnection().prepareStatement(ps_str);

            ps.setString(1, mfg);
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()) {
                String s = rs1.getString("prod");
                prod_data.add(s);
            }

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("AddProductController->fillProd " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();

        }
    }

    private void fillUnits(ObservableList<String> units_data) {
        units_data.clear();
        try {
            Statement stmt = Utils.getConnection().createStatement();
            String ps_str = "SELECT DISTINCT units FROM products "
                    + " WHERE 1 ORDER BY units ASC";
            PreparedStatement ps = Utils.getConnection().prepareStatement(ps_str);
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()) {
                String s = rs1.getString("units");
                units_data.add(s);
            }

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("AddProductController->fillUnits " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();

        }
    }

    public void setProdParams(String p, String m) {

        try {
            Statement stmt = Utils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE "
                    + " mfg =\"" + m + "\" "
                    + "AND prod =\"" + p + "\"");

            while (rs.next()) {
                String pc = rs.getString("prod_code");
                String u = rs.getString("units");
                String pr = rs.getString("price");
                double l = rs.getDouble("low_warn");
                String h = rs.getString("hsn_code");
                double g = rs.getDouble("gst");
                
                creat_price_txt.setText(pr);
                creat_low_warn_txt.setText(String.valueOf(l));
                creat_prod_code_txt.setText(pc);
                creat_hsn_code_txt.setText(h);
                creat_gst_txt.setText(String.valueOf(g));

              
                creat_units_cmb.setValue(u);
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("setProdParams " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }
    }

 
}
