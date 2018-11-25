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
public class CompDetailsController implements Initializable {

    @FXML
    private TextField bank_name;
    @FXML
    private TextField branch;
    @FXML
    private TextField acc_no;
    @FXML
    private TextField ifsc;
    @FXML
    private TextField term3;
    @FXML
    private TextField term1;
    @FXML
    private TextField term2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            String str1 = "SELECT * FROM bank_details";
            PreparedStatement pstmt = Utils.getConnection().prepareStatement(str1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bank_name.setText(rs.getString("name"));
                branch.setText(rs.getString("branch"));
                acc_no.setText(rs.getString("ac_no"));
                ifsc.setText(rs.getString("ifsc"));
            }
            String str2 = "SELECT * FROM terms";
            PreparedStatement pstmt1 = Utils.getConnection().prepareStatement(str2);
            ResultSet rs1 = pstmt1.executeQuery();

            while (rs1.next()) {
                if (rs1.getInt("id") == 1) {
                    term1.setText(rs1.getString("cond"));
                }
                if (rs1.getInt("id") == 2) {
                    term2.setText(rs1.getString("cond"));
                }
                if (rs1.getInt("id") == 3) {
                    term3.setText(rs1.getString("cond"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {

        try {
            String str = "UPDATE bank_details SET name = ?, branch = ?, ac_no = ?, "
                    + "ifsc = ? WHERE 1";
            PreparedStatement pstmt = Utils.getConnection().prepareStatement(str);
            pstmt.setString(1, bank_name.getText());
            pstmt.setString(2, branch.getText());
            pstmt.setString(3, acc_no.getText());
            pstmt.setString(4, ifsc.getText());
            pstmt.executeUpdate();

            String rts1 = "UPDATE terms SET cond = ? WHERE id = ?";
            PreparedStatement stmtp = Utils.getConnection().prepareStatement(rts1);
            stmtp.setString(1, term1.getText());
            stmtp.setInt(2, 1);
            stmtp.executeUpdate();
            stmtp.setString(1, term2.getText());
            stmtp.setInt(2, 2);
            stmtp.executeUpdate();
            stmtp.setString(1, term3.getText());
            stmtp.setInt(2, 3);
            stmtp.executeUpdate();
            
            
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
