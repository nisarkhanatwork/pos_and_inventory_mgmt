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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author nisar
 */
public class AdminController implements Initializable {

    @FXML
    private TextField ip_addr;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleResetBillNoAction(ActionEvent event) {
        try {
            String str1 = "UPDATE bill_no SET no=?, quote_no = ? WHERE id = ?";
            PreparedStatement stmt1 = Utils.getConnection().prepareStatement(str1);
            stmt1.setInt(1, 1);
            stmt1.setInt(2, 1);
            stmt1.setInt(3, 1);
            stmt1.executeUpdate();

            String str2 = "DELETE  FROM bills WHERE 1";
            PreparedStatement stmt2 = Utils.getConnection().prepareStatement(str2);
            stmt2.executeUpdate();
            
            String str3 = "DELETE FROM transport WHERE 1";
            PreparedStatement stmt3 = Utils.getConnection().prepareStatement(str3);
            stmt3.executeUpdate();
        } catch (SQLException e) {
        }
    }
}
