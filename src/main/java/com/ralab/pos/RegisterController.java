/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField prod_reg_email;
    @FXML
    private TextField prod_reg_pwd;
    @FXML
    private Button prod_reg_btn;
    @FXML
    private TextArea prod_reg_text_area;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Utils.checkReg()) {
            prod_reg_text_area.setText("This is a registered product!");
            prod_reg_email.setText(Utils.getRegEmail());
            disable_all();
        }
    }

    @FXML
    private void handleREGISTERPRODButtonAction(ActionEvent event) {
        String email = prod_reg_email.getText();
        if (email != null
                && email.indexOf('@') > 0
                && email.indexOf("gmail.com") > 0) {
            //gmail entered..send mail to server with details
            EmailReceiver receiver = new EmailReceiver();

            if (EmailReceiver.downloadEmailAttachments("pop.gmail.com", "995",
                    prod_reg_email.getText(),
                    prod_reg_pwd.getText()) != null) {
                Utils.setRegEmail(prod_reg_email.getText());
                Utils.setRegPwd(prod_reg_pwd.getText());
                Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setContentText("Registration successful..." );
                error.showAndWait();
                prod_reg_text_area.setText("Registration successful..");
                disable_all();

            } else {
                prod_reg_text_area.setText("Registration failed..");
            }

        }
    }

    public void disable_all() {
        prod_reg_email.setDisable(true);
        prod_reg_pwd.setDisable(true);
        prod_reg_btn.setDisable(true);
        prod_reg_text_area.setDisable(true);
    }

}
