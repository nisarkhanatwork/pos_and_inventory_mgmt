/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;


import static com.ralab.pos.OldBillingController.setTrStageState;

import static com.ralab.pos.OldBillingController.tr_stage;
import static com.ralab.pos.OldBillingController.oldbill;
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
public class OldTransportController implements Initializable {

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
        tr_name.setText(oldbill.tr.getTrName());
        tr_lrno.setText(oldbill.tr.getLrNo());
        tr_lrdate.setText(oldbill.tr.getLrDate());
        tr_bk_to.setText(oldbill.tr.getBkTo());
        tr_weight.setText(oldbill.tr.getWeight());
        tr_cases.setText(oldbill.tr.getCases());
        tr_pvtmark.setText(oldbill.tr.getPvtMark());
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        oldbill.tr.setTrName(tr_name.getText());
        oldbill.tr.setLrNo(tr_lrno.getText());
        oldbill.tr.setLrDate(tr_lrdate.getText());
        oldbill.tr.setBkTo(tr_bk_to.getText());
        oldbill.tr.setWeight(tr_weight.getText());
        oldbill.tr.setCases(tr_cases.getText());
        oldbill.tr.setPvtMark(tr_pvtmark.getText());

        tr_stage.close();
        setTrStageState(false);

    }

}
