/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  com.ralab.pos;

import java.math.BigInteger;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class CustomerController implements Initializable {

    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField city;
    @FXML
    private ComboBox state;
    @FXML
    private TextField gstno;
    @FXML
    private TextField ano;
    @FXML
    private TextField pno;
    @FXML
    private TextField query_phone;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        state.getItems().addAll(Utils.getStates());

    }

    @FXML
    protected void handleGetButtonAction(ActionEvent e) {
        try {
            Statement stmt = Utils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT *  FROM customer WHERE phone_no="
                    + query_phone.getText() + "");

            if (rs.next()) {
                fname.setText(rs.getString("first_name"));
                lname.setText(rs.getString("last_name"));
                city.setText(rs.getString("city"));
                state.setValue(rs.getString("state"));
                gstno.setText(rs.getString("gst"));
                ano.setText(rs.getString("aadhar_no"));
                pno.setText(rs.getString("phone_no"));

            } else {
            }
        }catch(Exception e1){
            
        }
    }

    @FXML
    protected void handleAddButtonAction(ActionEvent e) {
        String cust_info;
        if (!isDataOk()) {
            resetData();
        } else {

            try {
                Statement stmt = Utils.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT phone_no  FROM customer WHERE phone_no="
                        + pno.getText() + "");

                if (rs.next()) {
                    Alert error = new Alert(Alert.AlertType.INFORMATION);
                    error.setContentText("Customer Exists");
                    error.showAndWait();
                    cust_info = "UPDATE customer SET first_name = ?,"
                            + "last_name = ?,"
                            + "state = ?,"
                            + "city = ?,"
                            + "gst = ?,"
                            + "aadhar_no = ? WHERE phone_no = ?";

                } else {
                    //Insert Customer data
                    cust_info = "INSERT INTO customer (first_name, last_name"
                            + ", state, city, gst, aadhar_no, phone_no) VALUES (?,?,?,?,?,?,?)";
                }

                PreparedStatement pAddCustomer = Utils.getConnection1().prepareStatement(cust_info);

                pAddCustomer.setString(1, fname.getText());
                pAddCustomer.setString(2, lname.getText());
                pAddCustomer.setString(3, state.getValue().toString());
                pAddCustomer.setString(4, city.getText());
                pAddCustomer.setString(5, gstno.getText());
                pAddCustomer.setString(6, ano.getText());
                pAddCustomer.setString(7, pno.getText());

                pAddCustomer.executeUpdate();
                pAddCustomer = Utils.getConnection2().prepareStatement(cust_info);

                pAddCustomer.setString(1, fname.getText());
                pAddCustomer.setString(2, lname.getText());
                pAddCustomer.setString(3, state.getValue().toString());
                pAddCustomer.setString(4, city.getText());
                pAddCustomer.setString(5, gstno.getText());
                pAddCustomer.setString(6, ano.getText());
                pAddCustomer.setString(7, pno.getText());

                pAddCustomer.executeUpdate();
                resetData();

            } catch (SQLException e1) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("CustomerController Add Button " + e1.getMessage());
                error.showAndWait();
            }
        }
    }

    public boolean isDataOk() {
        int fname_len = fname.getText().length();
        int lname_len = fname.getText().length();
        int city_len = city.getText().length();
        String state_val = state.getValue().toString();
        int pno_len = pno.getText().length();
        int gst_len = gstno.getText().length();
        int aadhar_len = ano.getText().length();

//        if( (fname_len > 0 && fname_len <= 45) &&
//                (lname_len > 0 && lname_len <= 45) &&
//                (city_len >0 && city_len <= 45) &&
//                (!state_val.equals("....")) &&
//                (pno_len == 10) &&
//                (gst_len == 15) &&
//                (aadhar_len == 12))
//            return true;
        if (pno_len == 10) {
            return true;
        } else {
            return false;
        }
    }

    private void resetData() {
        fname.clear();
        lname.clear();
        city.clear();
        state.setValue(Utils.getStates().get(0));
        pno.clear();
        gstno.clear();
        ano.clear();

    }
}
