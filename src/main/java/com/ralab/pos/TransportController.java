/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import static com.ralab.pos.BillingController.bill;
import static com.ralab.pos.BillingController.setTrStageState;

import static com.ralab.pos.BillingController.tr_stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class TransportController implements Initializable {

    @FXML
    private TextField tr_name;
    @FXML
    private TextField tr_lrno;
    @FXML
    private TextField tr_lrdate;
    @FXML
    private TextField tr_weight;
    @FXML
    private TextField tr_bk_to;
    @FXML
    private TextField tr_cases;
    @FXML
    private TextField tr_pvtmark;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tr_name.setText(bill.tr.getTrName());
        tr_lrno.setText(bill.tr.getLrNo());
        tr_lrdate.setText(bill.tr.getLrDate());
        tr_bk_to.setText(bill.tr.getBkTo());
        tr_weight.setText(bill.tr.getWeight());
        tr_cases.setText(bill.tr.getCases());
        tr_pvtmark.setText(bill.tr.getPvtMark());
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        bill.tr.setTrName(tr_name.getText());
        bill.tr.setLrNo(tr_lrno.getText());
        bill.tr.setLrDate(tr_lrdate.getText());
        bill.tr.setBkTo(tr_bk_to.getText());
        bill.tr.setWeight(tr_weight.getText());
        bill.tr.setCases(tr_cases.getText());
        bill.tr.setPvtMark(tr_pvtmark.getText());

        tr_stage.close();
        setTrStageState(false);

    }

}
