/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;


import static com.ralab.pos.OldBillingController.bt_stage;
import static com.ralab.pos.OldBillingController.setBTText;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class OldBilledToController implements Initializable {

    @FXML
    private TableView<Address> addr_book2;
    /**
     * Initializes the controller class.
     */
    static ObservableList<Address> addr_data2 = FXCollections.observableArrayList( //            new BillItem( "1", "Tyre" , "Smih", "jom", "12", "pcs", "1240", "100")
            );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addr_book2.setItems(addr_data2);
        AddressBookController.addAddressesFromDB(addr_data2);

    }

    @FXML
    private void handleChooseCustomerButtonAction(ActionEvent event) {
        int sno = addr_book2.getSelectionModel().getSelectedItem().getSNo();
        bt_stage.close();
        OldBillingController.bt_stage_on = false;
        setBTText(Integer.toString(sno));
      
    }

}
