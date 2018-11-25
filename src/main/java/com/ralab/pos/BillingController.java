/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author lab149
 */
public class BillingController implements Initializable {

    @FXML
    private Text title;
    @FXML
    private TextField date;
    @FXML
    private Label date_label;
    @FXML
    private TextField time;
    @FXML
    private Label time_label;
    @FXML
    private TextField bill_no;
    @FXML
    private Label bill_no_label;
    @FXML
    private ComboBox<String> mfg;
    @FXML
    private ComboBox<String> prod;
    @FXML
    private TextField quant;
    @FXML
    private TextField rate;
    @FXML
    private TextField units;
    @FXML
    private TextField prod_code;
    @FXML
    private TextField hsn_code;
    @FXML
    private TextField gst;

    @FXML
    private CheckBox igst_chck_bx;
    @FXML
    private TextField phone_num;
    @FXML
    private TextField phone_num1;
    @FXML
    private TextField prod_code_in;
    @FXML
    private TextField tax;
    @FXML
    private Label tax_label;
    @FXML
    private CheckBox quote_chck_bx;
    @FXML
    private TableView<BillItem> billView;
    @FXML
    private Button del;
    @FXML
    private Button ok;
    @FXML
    private RadioButton tr_radio;
    @FXML
    private RadioButton prev_bills;
    @FXML
    private HBox hbox2;
    @FXML
    private TableColumn< BillItem, String> quant_col;
    @FXML
    private TableColumn< BillItem, String> rate_col;

    // Company details
    @FXML
    private TextField term1;
    @FXML
    private TextField term2;
    @FXML
    private TextField term3;

    @FXML
    private TextField bank_name;

    @FXML
    private TextField branch;
    @FXML
    private TextField acc_no;
    @FXML
    private TextField ifsc;

    
    Table[] table;

    Boolean ret_value = new Boolean(false);

    static boolean tr_stage_on = false;
    static Stage tr_stage;

    static boolean bt_stage_on = false;
    static Stage bt_stage;

    static boolean st_stage_on = false;
    static Stage st_stage;

    static StringProperty str_prop_phone_num;
    static StringProperty str_prop_phone_num1;
    /* Item Array */

    ObservableList<String> mfg_data = FXCollections.observableArrayList();
    ObservableList<String> prod_data = FXCollections.observableArrayList();
    static int item_no = 1;
    static int tamount_item_no = 2;
    static int tax_item_no = 3;
    static int total_item_no = 4;
    int size_of_bill = 0;
    MyBusiness bness;
    static Bill bill;

    List<BillItem> last_items = new ArrayList();
    private final int NO_OF_ITEMS = 20;
    private final int NO_OF_SUMMARY_ITEMS = 3;
    private DatePicker oldDatePicker;
    static int items_proc = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();

        title.setText(Utils.getCompanyName().toUpperCase() + " BILLING SYSTEM");
        date.setText(dateFormat.format(cal.getTime()));

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        time.setText(timeFormat.format(cal.getTime()));

        //init bill object
        bill = new Bill();

        bill_no.setText(bill.getBillNo());

        // init MyBusiness object
        bness = new MyBusiness();


        /* setting up bill table */
        billView.setEditable(true);

        /* START testing item addition from here */
        billView.setItems(bill.getItems());
        /* END */

        new AutoCompleteComboBoxListener(prod);
        new AutoCompleteComboBoxListener(mfg);
        //  prod.setEditable(true);

        mfg.setItems(mfg_data);
        prod.setItems(prod_data);
        prod_code.setFocusTraversable(false);
        hsn_code.setFocusTraversable(false);
        gst.setFocusTraversable(false);

        units.setFocusTraversable(false);
        rate.setFocusTraversable(false);
        date.setFocusTraversable(false);
        time.setFocusTraversable(false);
        bill_no.setFocusTraversable(false);
        billView.setFocusTraversable(false);
        del.setFocusTraversable(false);

        tax.setVisible(false);
        tax_label.setVisible(false);
        initMfgFromDB();

        //DEBUG 
        //   phone_num.setText("1");
//        phone_num.textProperty().addListener((observable, oldValue, newValue) -> {
//            changePhoneNoEntry(phone_num, newValue, bill.btCustomer);
//        });
//        phone_num1.textProperty().addListener((observable, oldValue, newValue) -> {
//            changePhoneNoEntry(phone_num1, newValue, bill.stCustomer);
//        });
        str_prop_phone_num = new SimpleStringProperty(this, "str_prop_phone_num", "");
        str_prop_phone_num1 = new SimpleStringProperty(this, "str_prop_phone_num1", "");
        str_prop_phone_num.addListener((obs, oldValue, newValue)
                -> {
            phone_num.setText(newValue);
            fillAddressData();
        });
        phone_num.textProperty().addListener((observable, oldValue, newValue) -> {
            fillAddressData();
        });
        str_prop_phone_num1.addListener((obs, oldValue, newValue)
                -> phone_num1.setText(newValue));
        prod_code_in.textProperty().addListener((observable, oldValue, newValue) -> {
            changeProdCode(prod_code_in, newValue);
                
        });
        tax.textProperty().addListener((observable, oldValue, newValue) -> {
            calcTax(tax, newValue);
        });
        setupQuantCol();
        setupRateCol();

    }

    @FXML
    private void handleMfgCoomboBoxAction(ActionEvent event) {

        if (mfg.getValue() != null) {
            prod_data.clear();
            setProdFromDBPerMfg(mfg.getValue());
        }
    }

    @FXML
    private void handleProdCoomboBoxAction(ActionEvent event) {
        if (prod.getValue() != null) {
            setProdID(prod.getValue().toString(),
                    mfg.getValue().toString());
        }
    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) {

        BigDecimal gsttax = BigDecimal.valueOf(Double.
                parseDouble(gst.getText())).divide(BigDecimal.valueOf(2.0));
        BigDecimal rtax = BigDecimal.ZERO;
        if (Utils.isRedBilling()) {
            if (Validator.isValidAmount(tax.getText())) {
                rtax = BigDecimal.valueOf(
                        Double.parseDouble(tax.getText()));
            }
        }
        BillItem b = new BillItem(String.valueOf(item_no),
                mfg.getValue().toString(),
                prod.getValue().toString(),
                quant.getText(),
                units.getText(),
                rate.getText(),
                prod_code.getText(),
                hsn_code.getText(),
                igst_chck_bx.isSelected() ? BigDecimal.ZERO.toString() : gsttax.toString(),
                igst_chck_bx.isSelected() ? BigDecimal.ZERO.toString() : gsttax.toString(),
                igst_chck_bx.isSelected() ? BigDecimal.valueOf(Double.
                parseDouble(gst.getText())).toString() : BigDecimal.ZERO.toString(),
                rtax.toString(),
                "100");
        if (!b.getAmountValue().equals("")) {

            addBillItem(b);
            addTotal();
        }

        //Calculate Total
        clearFields();
        prod_code_in.clear();

    }

    @FXML
    private void handleIGSTCheckBoxAction(ActionEvent event) {
        if (bill.getItems().size() > 0) {
            igst_chck_bx.setSelected(false);
        }
    }

    @FXML
    private void handleDeleteBillItemButtonAction(ActionEvent event) {
        ObservableList<BillItem> data = bill.getItems();

        int selected_item = billView.getSelectionModel().getSelectedIndex();
        //Check if the total is being removed 
        if (selected_item == (tax_item_no - 1)
                || (selected_item == (total_item_no - 1))
                || (selected_item == (tamount_item_no - 1))) {
            return;
        }

        bill.delItem(total_item_no - 1);
        bill.delItem(tax_item_no - 1);
        bill.delItem(tamount_item_no - 1);
        selected_item = billView.getSelectionModel().getSelectedIndex();

        bill.delItem(selected_item);

        ArrayList<BillItem> tmpBillItemArray = new ArrayList<>();
        bill.getItems().forEach(bill_item -> {
            tmpBillItemArray.add(bill_item);
        });

        bill.clear();
        resetItemNoAndTotalItemNo();
        tmpBillItemArray.forEach(tmp_item -> {
            tmp_item.setSerialNo(String.valueOf(item_no));
            addBillItemOnly(tmp_item);

        });
        if (tmpBillItemArray.size() != 0) {
            addTotal();
        }

    }

    @FXML
    private void handleOKButtonAction(ActionEvent event) {
        //set time here temporarily 
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        time.setText(timeFormat.format(cal.getTime()));

        if (popMsgBox()) {

            fillAddressData();
            fillLastItems();

            //print bills 0 - for orrig
            // 1 - for duplicate
            // 2 - for triplicate
            for (int i = 0; i < 3; i++) {
                if (bill.stCustomer.isDefault()
                        && bill.btCustomer.isDefault()) {

                    if (Utils.isRedBilling()) {
                        printToPdf(Utils.getSampleBillDir(), true, true, i);
                    } else {
                        printToPdf(Utils.getBillDir(), true, false, i);

                    }
                } else {
                    if (Utils.isRedBilling()) {
                        printToPdf(Utils.getSampleBillDir(), false, true, i);
                    } else {
                        printToPdf(Utils.getBillDir(), false, false, i);

                    }
                }
            }
            if (!quote_chck_bx.isSelected()) {
                if (Utils.isRedBilling()) {
                    java.util.Date dt = new java.util.Date();
                    java.text.SimpleDateFormat sdf
                            = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    bill.setTime(sdf.format(dt));
                    createJsonFile(bill);
                } else {
                    insertSalesIntoDB();
                }
            }

            resetItemNoAndTotalItemNo();
            unsetRetValue();
            if (!quote_chck_bx.isSelected()) {
                incrementBillNo();
            } else {
                incrementQuoteNo();
            }

            bill.clear();
            last_items.clear();
            igst_chck_bx.setSelected(false);
            quote_chck_bx.setSelected(false);

        } else {

        }
        tr_radio.setSelected(false);
        phone_num.clear();
        phone_num1.clear();
        tax.clear();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        bill.clear();
        resetItemNoAndTotalItemNo();
        clearFields();
        igst_chck_bx.setSelected(false);
        quote_chck_bx.setSelected(false);
        tr_radio.setSelected(false);
        bill.clearTransport();
        tax.clear();
    }

    @FXML
    private void handleTransportRadioButtonAction(ActionEvent event) {
        if (tr_radio.isSelected()) {
            if (!tr_stage_on) {
                showTransport();
                tr_stage_on = true;
            } else {
                tr_stage.toFront();
            }
        } else {

        }

    }

    @FXML
    private void handleBilledToButtonAction(ActionEvent event) {
        if (!bt_stage_on) {
            showBilledTo();
            bt_stage_on = true;
        } else {
            bt_stage.toFront();
        }
    }

    @FXML
    private void handleShippedToButtonAction(ActionEvent event) {
        if (!st_stage_on) {
            showShippedTo();
            st_stage_on = true;
        } else {
            st_stage.toFront();
        }
    }

    public void showTransport() {

        tr_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Transport.fxml"));
            Scene scene = new Scene(root, 600, 400);

            tr_stage.setTitle(Utils.getCompanyName() + " Software");
            tr_stage.setScene(scene);
            tr_stage.setResizable(false);

            tr_stage.show();

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showTransport " + e.getMessage());
            error.showAndWait();
        }
        tr_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                tr_stage_on = false;
                tr_radio.setSelected(false);
            }

        });

    }

    public void showBilledTo() {

        bt_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/BilledTo.fxml"));
            Scene scene = new Scene(root, 1100, 600);

            bt_stage.setTitle(Utils.getCompanyName() + " Software");
            bt_stage.setScene(scene);
            bt_stage.setResizable(false);

            bt_stage.show();

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showTransport " + e.getMessage());
            error.showAndWait();
        }
        bt_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                bt_stage_on = false;

            }

        });

    }

    public void showShippedTo() {

        st_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ShippedTo.fxml"));
            Scene scene = new Scene(root, 1100, 600);

            st_stage.setTitle(Utils.getCompanyName() + " Software");
            st_stage.setScene(scene);
            st_stage.setResizable(false);

            st_stage.show();

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showShippedTo " + e.getMessage());
            error.showAndWait();
        }
        st_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                st_stage_on = false;

            }

        });

    }

    private void addTotal() {
        ObservableList<BillItem> data = bill.getItems();
        List<String> amounts = new ArrayList<>();
        List<BigDecimal> taxes = new ArrayList<>();

        bill.getItems().forEach(bill_item -> {
            final BigDecimal bd = BigDecimal.valueOf(Double.
                    parseDouble(bill_item.getAmountValue()));
            amounts.add(bd.toString());
            taxes.add(bill_item.getTaxAmount());
        });
        BigDecimal tbd = new BigDecimal(0.0);
        int l = amounts.size();
        for (String a : amounts) {
            tbd = tbd.add(BigDecimal.valueOf(
                    Double.parseDouble(a)));

        }

        BigDecimal taxbd = new BigDecimal(0.0);
        for (BigDecimal t : taxes) {
            taxbd = taxbd.add(t);
        }
        BillItem subTotalBillItem = new BillItem("Sub Total", "", "",
                "", "", "", "", "", "", "", "", "", tbd.toString());
        BillItem taxBillItem = null;
        if (Utils.isRedBilling()) {
            BigDecimal redtax = BigDecimal.ZERO;
            taxbd = BigDecimal.ZERO;
            if (tax.getText().length() == 0
                    || !Validator.isValidAmount(tax.getText())) {

            } else {

                redtax = BigDecimal.valueOf(Double.valueOf(tax.getText()));
                taxbd = taxbd.add(redtax);
            }
            taxBillItem = new BillItem("Tax", "", "",
                    "", "", "", "", "", "", "", "", "", redtax.toString());

        } else {
            taxBillItem = new BillItem("Tax", "", "",
                    "", "", "", "", "", "", "", "", "", Utils.isGSTEnabled() ? taxbd.toString() : BigDecimal.ZERO.toString());
        }
        BillItem totalBillItem = new BillItem("Total", "", "",
                "", "", "", "", "", "", "", "", "", tbd.add(
                        Utils.isGSTEnabled() ? taxbd : BigDecimal.ZERO).toString());

        tamount_item_no = item_no;
        tax_item_no = tamount_item_no + 1;
        total_item_no = tax_item_no + 1;
        bill.addItem(tamount_item_no - 1, subTotalBillItem);
        bill.addItem(tax_item_no - 1, taxBillItem);
        bill.addItem(total_item_no - 1, totalBillItem);

    }

    public void addBillItem(BillItem bItem) {
        try {
            if (bill.getSize() != 0) {
                // data.get(total_item_no - 1);
                bill.delItem(total_item_no - 1);
                //data.get(tax_item_no - 1);
                bill.delItem(tax_item_no - 1);
                // data.get(tamount_item_no - 1);
                bill.delItem(tamount_item_no - 1);
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        bill.addItem(item_no - 1, bItem);
        item_no++;

    }

    public void addBillItemOnly(BillItem bItem) {

        bill.addItem(item_no - 1, bItem);
        item_no++;

    }

    public void resetItemNoAndTotalItemNo() {
        item_no = 1;
        tamount_item_no = 2;
        tax_item_no = 3;
        total_item_no = 4;

    }

    public boolean popMsgBox() {

        Alert print = new Alert(Alert.AlertType.CONFIRMATION);
        print.setContentText("Do you want to print the Bill ?");
        print.setHeaderText("");
        print.setTitle("Confirm Bill");
        print.showAndWait()
                .filter(response -> response.equals(ButtonType.OK))
                .ifPresent(response -> setRetValue());
        return ret_value;

    }

    public void setRetValue() {
        ret_value = true;
    }

    public void unsetRetValue() {
        ret_value = false;
    }

    public void incrementBillNo() {

        String bill_no_from_db = new String(bill.getBillNo());

        String new_bill_no = new String(String.valueOf(Integer.parseInt(bill_no_from_db) + 1));
        bill.updateBillNoInDB(new_bill_no, false);
        bill_no.setText(new_bill_no);
    }

    public void incrementQuoteNo() {

        String quote_no_from_db = new String(bill.getQuoteNo());

        String new_quote_no = new String(String.valueOf(Integer.parseInt(quote_no_from_db) + 1));
        bill.updateQuoteNoInDB(new_quote_no, false);
    }

    public void writeToFile(String data_to_file, String fName) {
        DataOutputStream dos;
        try {
            File outFile = new File(fName);

            dos = new DataOutputStream(new FileOutputStream(outFile));
            dos.writeBytes(data_to_file);
            dos.close();

        } catch (FileNotFoundException e3) {
            Alert print_error = new Alert(Alert.AlertType.ERROR);
            print_error.setContentText("File not Found in writeToFile");
            print_error.showAndWait();
        } catch (IOException e4) {
            Alert print_error = new Alert(Alert.AlertType.ERROR);
            print_error.setContentText("IOException in writeToFile");
            print_error.showAndWait();

        }
    }

    public void initMfgFromDB() {
        try {
            Statement stmt = Utils.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT mfg  FROM products"
                    + " ORDER by mfg ASC");

            while (rs.next()) {
                String s = rs.getString("mfg");
                mfg_data.add(s);
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("initMfgFromDB" + e.getMessage());
            error.showAndWait();
        }
    }

    public void setProdFromDBPerMfg(String sMfg) {
        try {
            String str1 = "SELECT DISTINCT prod FROM products WHERE "
                    + "mfg = ?  ORDER by prod ASC";
            PreparedStatement stmt1 = Utils.getConnection().prepareStatement(str1);
            stmt1.setString(1, sMfg);
            ResultSet rs = stmt1.executeQuery();

            while (rs.next()) {
                String s = rs.getString("prod");
                prod_data.add(s);
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("setProdFromDBPerMfg " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }
    }

    public void setProdID(String p, String m) {
        try {

            String str1 = "SELECT hsn_code, gst, "
                    + " prod_code, units, price FROM products WHERE mfg = ?  AND prod = ?";

            PreparedStatement stmt1 = Utils.getConnection().prepareStatement(str1);
            stmt1.setString(1, m);
            stmt1.setString(2, p);

            ResultSet rs = stmt1.executeQuery();
//            
//            ResultSet rs = stmt.executeQuery("SELECT hsn_code, cgst, sgst, "
//                    + " prod_code, unitscombination, price FROM products WHERE"
//                    + " mfg =\"" + m + "\" "
//                    + "AND prod =\"" + p + "\"");
//
            while (rs.next()) {
                String h = rs.getString("hsn_code");
                String g = rs.getString("gst");
                String pc = rs.getString("prod_code");
                String u = rs.getString("units");
                String r = rs.getString("price");

                prod_code.setText(pc);

                //set the same in prod_code_in
                prod_code_in.setText(pc);
                hsn_code.setText(h);
                gst.setText(g);

                units.setText(u);
                rate.setText(r);
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("setProdID " + e.getMessage());
            error.showAndWait();
        }
    }

    public void clearFields() {

        mfg_data.clear();
        mfg.setValue(null);

        initMfgFromDB();
        prod_data.clear();
        prod.setValue(null);

        quant.clear();
        units.clear();
        rate.clear();
        prod_code.clear();
        hsn_code.clear();
        gst.clear();
        prod_code_in.requestFocus();

    }

    private void insertSalesIntoDB() {

        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int cid = bill.btCustomer.getSNo();
        int sid = bill.stCustomer.getSNo();
        String currentTime = sdf.format(dt);
        boolean is_gst = Utils.isGSTEnabled();

        bill.getItems().forEach(bi -> {
            try {
                if (!(bi.getSerialNo().equals("Total")
                        || bi.getSerialNo().equals("Sub Total")
                        || bi.getSerialNo().equals("Tax"))) {
                    String str1 = "INSERT INTO bills (date_time, "
                            + "s_no, bill_no, prod_code, quant, amount, rate, phone_num,"
                            + " phone_num1, is_gst, is_igst, gst, tax) "
                            + "VALUES ( ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt1 = Utils.getConnection().prepareStatement(str1);
                    stmt1.setString(1, currentTime);
                    stmt1.setInt(2, Integer.parseInt(bi.getSerialNo()));
                    stmt1.setInt(3, Integer.parseInt(bill_no.getText()));
                    stmt1.setString(4, bi.getProdCode());
                    stmt1.setDouble(5, Double.parseDouble(bi.getQuantValue()));
                    stmt1.setDouble(6, Double.parseDouble(bi.getAmountValue()));
                    stmt1.setDouble(7, Double.parseDouble(bi.getRateValue()));
                    stmt1.setString(8, Integer.toString(cid));
                    stmt1.setString(9, Integer.toString(sid));
                    stmt1.setBoolean(10, is_gst);
                    stmt1.setBoolean(11, igst_chck_bx.isSelected());
                    stmt1.setDouble(12, Double.parseDouble(bi.getGst()));
                    stmt1.setDouble(13, Double.parseDouble(bi.getRtax()));

                    stmt1.executeUpdate();

//                    Statement stmt = Utils.getConnection().createStatement();
//                    int ret = stmt.executeUpdate("INSERT INTO bills (date_time, "
//                            + "s_no, bill_no, prod_code, quant, amount, phone_num,"
//                            + " is_gst, is_igst) "
//                            + "VALUES ( \'"
//                            + currentTime + "\', "
//                            + Integer.parseInt(bi.getSerialNo()) + ", "
//                            + Integer.parseInt(bill_no.getText()) + ", \""
//                            + bi.getProdCode() + "\", "
//                            + Double.parseDouble(bi.getQuantValue()) + ", "
//                            + Double.parseDouble(bi.getAmountValue()) + ","
//                            + ph_no + ", "
//                            + is_gst + " , "
//                            + igst_chck_bx.isSelected() + " )");
                }

            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("insertSalesIntoDB " + e.getMessage());
                error.showAndWait();
            }
        });

        //insert into transport table
//        if (bill.tr.getTrName().length() != 0
//                || bill.tr.getLrNo().length() != 0
//                || bill.tr.getLrDate().length() != 0
//                || bill.tr.getFreight().length() != 0
//                || bill.tr.getWeight().length() != 0
//                || bill.tr.getCases().length() != 0
//                || bill.tr.getPvtMark().length() != 0) {
        try {
            String tr_str = "INSERT INTO transport (bill_no, name,"
                    + "lrno, lrdate, weight, cases, pvtmark, bk_to, bt)"
                    + "VALUES (?,?,?,?,?,?,?,?, ?)";
            PreparedStatement tr_stmt = Utils.getConnection().prepareCall(tr_str);
            tr_stmt.setInt(1, Integer.parseInt(bill_no.getText()));
            tr_stmt.setString(2, bill.tr.getTrName());
            tr_stmt.setString(3, bill.tr.getLrNo());
            tr_stmt.setString(4, bill.tr.getLrDate());
            //  tr_stmt.setString(5, bill.tr.getFreight());
            tr_stmt.setString(5, bill.tr.getWeight());
            tr_stmt.setString(6, bill.tr.getCases());
            tr_stmt.setString(7, bill.tr.getPvtMark());
            tr_stmt.setString(8, bill.tr.getBkTo());
            tr_stmt.setInt(9, bill.btCustomer.getSNo());
            tr_stmt.executeUpdate();
            bill.clearTransport();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // }
    }

    public void printToPdf(String fileName, boolean no_data, boolean is_red, int type) {

        // String dest = fileName + ".pdf";
        Integer num = quote_chck_bx.isSelected() ? Integer.parseInt(bill.getQuoteNo())
                : Integer.parseInt(bill_no.getText());
        String bill_or_quote = quote_chck_bx.isSelected() ? "quote_" : "invoice_";
        String bill_or_quote1 = quote_chck_bx.isSelected() ? " Quotation " : " Invoice ";
        char[] type_char = {'O', 'D', 'T'};

        String dest = fileName + bill_or_quote + String.format("%04d", num)
                + "_" + type_char[type] + ".pdf";
        size_of_bill = bill.getSize();
        int num_tables = (size_of_bill - NO_OF_SUMMARY_ITEMS) / NO_OF_ITEMS
                + ((((size_of_bill - NO_OF_SUMMARY_ITEMS) % NO_OF_ITEMS) != 0) ? 1 : 0);
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

            Table tf_table = add_to_from(no_data);
            //For page header
            VariableHeaderEventHandler handler = new VariableHeaderEventHandler(false,
                    true, tf_table, type);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            Document doc = new Document(pdfDoc);
            doc.setMargins(20, 20, 20, 20);

            //For page header
            handler.setHeader(Utils.getCompanyName());

            Rectangle[] areas = new Rectangle[]{
                //  new Rectangle(20, 20, 540, 740)};
                new Rectangle(20, 20, 540, 740)};

            // for canvas usage one should create a page
            PdfPage pdfPage = pdfDoc.addNewPage();
            for (Rectangle rect : areas) {
                new PdfCanvas(pdfDoc
                        .getFirstPage())
                        .setLineWidth(0.5f)
                        .setStrokeColor(Color.WHITE)
                        .rectangle(rect).stroke();
            }
            doc.setRenderer(new ColumnDocumentRenderer(doc, areas));

            String no = String.format("%04d", num);
            Paragraph o0 = new Paragraph(bill_or_quote1 + ": ")
                    .add(no).setBold().setFontColor(Color.BLACK)
                    .setFirstLineIndent(460).setMarginBottom(0f);
            Paragraph o3 = new Paragraph();
            Paragraph o4 = new Paragraph().setUnderline(2, 650);

            for (int i = 0; i < num_tables; i++) {
                doc.add(o0);
                doc.add(add_gst_date());
                tf_table.setBorderTop(new SolidBorder(Color.BLACK, 2));
                tf_table.setBorderBottom(new SolidBorder(Color.BLACK, 2));
                doc.add(tf_table);

                //doc.setUnderline(2, 620);
                doc.add(o3);

                if (is_red) {
                    doc.add(add_items_red(i));
                } else {
                    doc.add(add_items(i));
                }
                doc.add(add_end(igst_chck_bx.isSelected(), (i == num_tables - 1) ? true : false));
//                Paragraph o5 = new Paragraph(" ").add(" ");
//                Paragraph o6 = new Paragraph("RA LABS").add(" ");
//                doc.add(o5).add(o6);
            }
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Table add_to_from(boolean no_data) {

        float[] columnWidths = {325, 125};
        Table table = new Table(columnWidths);
        table.setBorder(Border.NO_BORDER);
        table.setWidthPercent(100);
        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell1f = new Cell(1, 1)
                    .add(new Paragraph("Billed To:"))
                    .setFont(f)
                    .setFontSize(9)
                    .setBold()
                    .setBorder(Border.NO_BORDER).setFontColor(Color.BLACK);
            Cell cell2 = new Cell(1, 1)
                    .add(new Paragraph("")) // used to be "shipped to"
                    .setFont(f)
                    .setFontSize(9)
                    .setBold()
                    .setBorder(Border.NO_BORDER).setFontColor(Color.BLACK);
            Cell cell3 = new Cell(1, 1)
                    .add(new Paragraph("Transportation: "))
                    .setFont(f)
                    .setFontSize(9)
                    .setBold()
                    .setBorder(Border.NO_BORDER).setFontColor(Color.BLACK);

            table.addHeaderCell(cell1f)
                    //   .addHeaderCell(cell2)
                    .addHeaderCell(cell3);
//Start Add Data from Database
            String fname1 = bill.btCustomer.getPName();
//            String lname1 = bill.btCustomer.getLName().substring(0,
//                    bill.btCustomer.getLName().length() <= 14 ? bill.btCustomer.getLName().length()
//                    : 14);
            String city1 = bill.btCustomer.getAddr1();
            String state1 = bill.btCustomer.getAddr2();
            String pno1 = bill.btCustomer.getPhone();
            String gst1 = bill.btCustomer.getTin();
            String ano1 = bill.btCustomer.getANo();

            String fname2 = bill.stCustomer.getPName().substring(0,
                    bill.stCustomer.getPName().length() <= 15 ? bill.stCustomer.getPName().length()
                    : 15);
//            String lname2 = bill.stCustomer.getLName().substring(0,
//                    bill.stCustomer.getLName().length() <= 14 ? bill.stCustomer.getLName().length()
//                    : 14);
            String city2 = bill.stCustomer.getAddr1().substring(0,
                    bill.stCustomer.getAddr1().length() <= 20 ? bill.stCustomer.getAddr1().length()
                    : 20);
            String state2 = bill.stCustomer.getAddr2().substring(0,
                    bill.stCustomer.getAddr2().length() <= 20 ? bill.stCustomer.getAddr2().length()
                    : 20);
            String pno2 = bill.stCustomer.getPhone().substring(0,
                    bill.stCustomer.getPhone().length() <= 22 ? bill.stCustomer.getPhone().length()
                    : 22);
            String gst2 = bill.stCustomer.getTin().substring(0,
                    bill.stCustomer.getTin().length() <= 15 ? bill.stCustomer.getTin().length()
                    : 15);
            String ano2 = bill.stCustomer.getANo().substring(0,
                    bill.stCustomer.getANo().length() <= 12 ? bill.stCustomer.getANo().length()
                    : 12);
//GST special bills
            String our_gst = new String(Utils.getGstNo());

            if (!Utils.isGSTEnabled()) {
                our_gst = new String("");
                gst1 = new String("");
                gst2 = new String("");
            }
            if (no_data) {
                fname1 = "";
//                lname1 = "";
                city1 = "";
                state1 = "";
                ano1 = "";
                pno1 = "";

                fname2 = "";
//                lname2 = "";
                city2 = "";
                state2 = "";
                ano2 = "";
                pno2 = "";

            }

//End Add Data from Database
            PdfFont f12 = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            PdfFont b2 = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            Cell cell_from = new Cell(1, 1)
                    .add(
                            fname1 + "\n"
                            + city1 + "\n"
                            + state1 + "\n"
                            + "Phone: " + pno1 + "\n"
                            + "GSTIN: " + gst1 + "\n"
                            + "Aadhar No: " + ano1)
                    .setFontSize(9)
                    .setMargins(0, 0, 0, 0)
                    .setFont(f12)
                    .setBorder(Border.NO_BORDER).setMinHeight(70);
            cell_from.setTextAlignment(TextAlignment.LEFT);

            Cell cell_to = new Cell(1, 1).add(fname2 + "\n"
                    //                    + lname2 + "\n"
                    + city2 + "\n"
                    + state2 + "\n"
                    + "" + pno2 + "\n"
                    + "" + gst2 + "\n"
                    + "" + ano2).setFontSize(7)
                    .setBorder(Border.NO_BORDER).setMinHeight(70);
            cell_to.setTextAlignment(TextAlignment.LEFT);

            Cell cell_trans = new Cell(1, 1).add(
                    "BOOKING TO: " + bill.tr.getBkTo() + "\n"
                    + "TRANSPORT: " + bill.tr.getTrName() + "\n"
                    + "L.R Number: " + bill.tr.getLrNo() + "\n"
                    //  + "L.R DATE:     " + bill.tr.getLrDate() + "\n"
                    //  + "WEIGHT: " + bill.tr.getWeight() + "\n"
                    + "CASES: " + bill.tr.getCases() + "\n"
            //  + "PVT MARK: " + bill.tr.getPvtMark() + "\n"
            ).setFontSize(7)
                    .setBorder(Border.NO_BORDER).setMinHeight(70);
            cell_trans.setTextAlignment(TextAlignment.LEFT);

            table.addCell(cell_from)
                    // .addCell(cell_to)
                    .addCell(cell_trans);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public Table add_gst_date() {

        float[] columnWidths = {225, 225};
        Table table = new Table(columnWidths);
        table.setBorder(Border.NO_BORDER);
        table.setWidthPercent(100);
        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell1f = new Cell(1, 1)
                    .add(new Paragraph("GSTIN: " + Utils.getGstNo()))
                    // .setFont(f)
                    .setFontSize(9)
                    .setBold()
                    .setBorder(Border.NO_BORDER)
                    .setFontColor(Color.BLACK);
            Cell cell2 = new Cell(1, 1)
                    .add(new Paragraph(date.getText()).setFirstLineIndent(163)
                    )
                    //   .setFont(f)
                    .setFontSize(9)
                    .setBold()
                    .setBorder(Border.NO_BORDER)
                    .setFontColor(Color.BLACK);

            table.addCell(cell1f.setTextAlignment(TextAlignment.LEFT))
                    .addCell(cell2.setTextAlignment(TextAlignment.LEFT));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public Table add_items(int serial) {
        size_of_bill = bill.getSize();
        Color bg = new DeviceRgb(244, 244, 244);
        Cell cell1, cell5, cell3, cell4, cell6, cell7, cell8, cell9, cell12;
        cell1 = cell3 = cell5 = cell4 = cell6
                = cell7 = cell8 = cell9 = cell12 = new Cell(1, 1);
        float[] col = {0f, 0f};
        Table table = new Table(col);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            cell1 = new Cell(1, 1)
                    // .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SNO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell5 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Code"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

//            cell3 = new Cell(1, 1)
//                    //   .setBorderLeft(Border.NO_BORDER)
//                    //    .setBorderTop(Border.NO_BORDER)
//                    //   .setBorderRight(Border.NO_BORDER)
//                    .add(new Paragraph("Mfg"))
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                    .setBackgroundColor(bg)
//                    .setFont(f)
//                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell4 = new Cell(1, 1)
                    //    .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Product"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell6 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("HSN\nCode"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell7 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Quant/\nUnits"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//            cell8 = new Cell(1, 1)
//                    //   .setBorderLeft(Border.NO_BORDER)
//                    //  .setBorderTop(Border.NO_BORDER)
//                    //  .setBorderRight(Border.NO_BORDER)
//                    .add(new Paragraph("Units"))
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                    .setBackgroundColor(bg)
//                    .setFont(f)
//                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell9 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //  .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Rate"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell12 = new Cell(1, 1)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph(" Taxable \n Amount"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            if (igst_chck_bx.isSelected()) {
                float[] columnWidths = {3, 6, 24, 7, 7, 7, 12, 7, 12};
                table = new Table(columnWidths);
                table.setWidthPercent(100);

                Image itext = new Image(ImageDataFactory.create(getClass()
                        .getResource("/images/" + "rupee.png")))
                        .setWidth(5).setHeight(7);

//                float[] columnWidths_small = {5, 8};
//                Table table_small = new Table(columnWidths_small);
//                table_small.setWidthPercent(100);
//                Cell cell11_1 = new Cell(1, 2)
//                        .add(new Paragraph("IGST"))
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setBackgroundColor(bg)
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                Cell cell11_1_1 = new Cell(1, 1)
                        .add(new Paragraph("IGST\nRate"))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBackgroundColor(bg)
                        .setFont(f)
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                Cell cell11_1_2 = new Cell(1, 1)
                        .add(new Paragraph("IGST\nAmt"))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBackgroundColor(bg)
                        .setFont(f)
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);

//                Cell cell11_1_0 = new Cell(1, 2)
//                        .add(table_small)
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setBackgroundColor(bg)
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK).setPadding(0).setBorder(Border.NO_BORDER);
//
//                table_small.setBorder(Border.NO_BORDER).addCell(cell11_1).addCell(cell11_1_1).addCell(cell11_1_2);
                table.addHeaderCell(cell1)
                        .addHeaderCell(cell5)
                        .addHeaderCell(cell4)
                        .addHeaderCell(cell6)
                        .addHeaderCell(cell7)
                        .addHeaderCell(cell9)
                        .addHeaderCell(cell12)
                        .addHeaderCell(cell11_1_1)
                        .addHeaderCell(cell11_1_2);
                fill_table(table, true, serial);

            } else {
                float[] columnWidths = {3, 6, 24, 7, 7, 7, 12, 7, 12};
                table = new Table(columnWidths);
                table.setWidthPercent(100);

                Image itext = new Image(ImageDataFactory.create(getClass()
                        .getResource("/images/" + "rupee.png")))
                        .setWidth(5).setHeight(7);
//                float[] columnWidths_small = {5, 8};
//                Table table_small1 = new Table(columnWidths_small);
//                table_small1.setWidthPercent(100);
//                Table table_small2 = new Table(columnWidths_small);
//                table_small2.setWidthPercent(100);
//                Cell cell10_1 = new Cell(1, 2)
//                        //       .setBorderLeft(Border.NO_BORDER)
//                        //       .setBorderTop(Border.NO_BORDER)
//                        // .setBorderRight(Border.NO_BORDER)
//                        //          .setBorderBottom(Border.NO_BORDER)
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .add(new Paragraph("GST"))
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                Cell cell10_1_1 = new Cell(1, 1)
                        //     .setBorderLeft(Border.NO_BORDER)
                        //     .setBorderTop(Border.NO_BORDER)
                        //     .setBorderRight(Border.NO_BORDER)
                        //      .setBorderBottom(Border.NO_BORDER)
                        .add(new Paragraph("GST\nRate"))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBackgroundColor(bg)
                        .setFont(f)
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
                Cell cell10_1_2 = new Cell(1, 1)
                        //     .setBorderLeft(Border.NO_BORDER)
                        //      .setBorderTop(Border.NO_BORDER)
                        // .setBorderRight(Border.NO_BORDER)
                        .add(new Paragraph("GST\nAmt"))
                        //        .setBorderBottom(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBackgroundColor(bg)
                        .setFont(f)
                        .setFontSize(9).setBold().setFontColor(Color.BLACK);

//                Cell cell10_1_0 = new Cell(1, 2)
//                        //    .setBorderLeft(Border.NO_BORDER)
//                        //    .setBorderTop(Border.NO_BORDER)
//                        //    .setBorderRight(Border.NO_BORDER)
//                        .add(table_small1)
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setBackgroundColor(bg)
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK).setPadding(0).setBorder(Border.NO_BORDER);
//
//                table_small1.setBorder(Border.NO_BORDER).addCell(cell10_1).addCell(cell10_1_1).addCell(cell10_1_2).setMarginLeft(0);
//                Cell cell11_1 = new Cell(1, 2)
//                        //    .setBorderLeft(Border.NO_BORDER)
//                        //     .setBorderTop(Border.NO_BORDER)
//                        //      .setBorderRight(Border.NO_BORDER)
//                        //    .setBorderBottom(Border.NO_BORDER)
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .add(new Paragraph("SGST"))
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                Cell cell11_1_1 = new Cell(1, 1)
//                        //    .setBorderLeft(Border.NO_BORDER)
//                        //    .setBorderTop(Border.NO_BORDER)
//                        //     .setBorderRight(Border.NO_BORDER)
//                        //     .setBorderBottom(Border.NO_BORDER)
//                        .add(new Paragraph("Rate"))
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setBackgroundColor(bg)
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                Cell cell11_1_2 = new Cell(1, 1)
//                        //      .setBorderLeft(Border.NO_BORDER)
//                        //      .setBorderTop(Border.NO_BORDER)
//                        //       .setBorderRight(Border.NO_BORDER)
//                        .add(new Paragraph("Amt"))
//                        //      .setBorderBottom(Border.NO_BORDER)
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setBackgroundColor(bg)
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK);
//
//                Cell cell11_1_0 = new Cell(1, 2)
//                        //     .setBorderLeft(Border.NO_BORDER)
//                        //      .setBorderTop(Border.NO_BORDER)
//                        //      .setBorderRight(Border.NO_BORDER)
//                        .add(table_small2)
//                        .setTextAlignment(TextAlignment.CENTER)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setBackgroundColor(bg)
//                        .setFont(f)
//                        .setFontSize(9).setBold().setFontColor(Color.BLACK).setPadding(0).setBorder(Border.NO_BORDER);
//                table_small2.setBorder(Border.NO_BORDER)
//                        .addCell(cell11_1)
//                        .addCell(cell11_1_1)
//                        .addCell(cell11_1_2)
//                        .setMarginLeft(0);
                table.addHeaderCell(cell1)
                        .addHeaderCell(cell5)
                        //  .addHeaderCell(cell3)
                        .addHeaderCell(cell4)
                        .addHeaderCell(cell6)
                        .addHeaderCell(cell7)
                        // .addHeaderCell(cell8)
                        .addHeaderCell(cell9)
                        .addHeaderCell(cell12)
                        .addHeaderCell(cell10_1_1)
                        .addHeaderCell(cell10_1_2);
                //  .addHeaderCell(cell11_1_0);
                fill_table(table, false, serial);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return table;

    }

    public boolean isBillEmpty() {
        return (bill.getSize() == 0);
    }

    private void changePhoneNoEntry(TextField phone, String pn, Customer currCustomer) {
        if (pn.length() == 10) {
            try {

                String str = "SELECT phone_no, first_name, last_name, "
                        + "state, city, gst, aadhar_no  FROM customer WHERE phone_no=?";

                PreparedStatement stmt = Utils.getConnection().prepareStatement(str);
                stmt.setString(1, pn);

//                Statement stmt = Utils.getConnection().createStatement();
//                
//                ResultSet rs = stmt.executeQuery("SELECT phone_no, first_name, last_name, "
//                        + "state, city, gst, aadhar_no  FROM customer WHERE phone_no="
//                        + pn);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String pno = rs.getString("phone_no");
                    String fname = rs.getString("first_name");
                    String lname = rs.getString("last_name");
                    String state = rs.getString("state");
                    String city = rs.getString("city");
                    String gst = rs.getString("gst");
                    String ano = rs.getString("aadhar_no");
                    Tooltip tt = new Tooltip("Phone Number: " + pno + "\n"
                            + "First Name: " + fname + "\n"
                            + "Last Name: " + lname + "\n"
                            + "State: " + state + "\n"
                            + "City: " + city + "\n"
                            + "GST No.: " + gst + "\n"
                            + "Aadhar No.: " + ano);
                    currCustomer.setFName(fname);
                    currCustomer.setLName(lname);
                    currCustomer.setCity(city);
                    currCustomer.setState(state);
                    currCustomer.setPhoneNo(pno);
                    currCustomer.setGstNo(gst);
                    currCustomer.setANo(ano);

                    phone.setTooltip(tt);

                } else {
                    Tooltip tt = new Tooltip("Customer not recognized!");
                    phone.setStyle("-fx-text-inner-color: red;");
                    phone.setTooltip(tt);
                    currCustomer.setPhoneNo(Customer.dummyPhone);

                }
            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("initTypeFromDB" + e.getMessage());
                error.showAndWait();
            }
        } else {
            Tooltip tt = new Tooltip("Customer not recognized!");
            phone.setTooltip(tt);
            currCustomer.setPhoneNo(Customer.dummyPhone);

            phone.setStyle("-fx-text-inner-color: black;");
        }
    }

    private void changeProdCode(TextField prod_in, String pn) {
        if (pn.length() != 0) {
       
            try {

                String str = "SELECT * FROM products "
                        + " WHERE prod_code=?";

                PreparedStatement stmt = Utils.getConnection().prepareStatement(str);
                stmt.setString(1, pn);

//                Statement stmt = Utils.getConnection().createStatement();
//                
//                ResultSet rs = stmt.executeQuery("SELECT phone_no, first_name, last_name, "
//                        + "state, city, gst, aadhar_no  FROM customer WHERE phone_no="
//                        + pn);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                String mfg_str = new String();
                String prod_str = new String();
                if (rs.next()) {
                    mfg_str = rs.getString("mfg");
                    prod_str = rs.getString("prod");
                    count++;
                }
                if (count == 1) {
                    mfg.setValue(mfg_str);
                    prod.setValue(prod_str);
                    prod_in.setStyle("-fx-text-inner-color: black;");

                } else {
                    Tooltip tt = new Tooltip("Product not recognized!");
                    prod_in.setStyle("-fx-text-inner-color: red;");
                    prod_in.setTooltip(tt);
                    clearFields();

                }
            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("changeProdCode" + e.getMessage());
                error.showAndWait();
            }
        } else {
            clearFields();
            prod_in.setStyle("-fx-text-inner-color: black;");
        }
    }

    //DANGER 4 functions need to be modified if you change this
    //The following have same functionality
    // 1) BillingController.fill_table
    // 2) BillingController.fill_table_red
    // 3) OldBillingController.fill_table
    // 4) OldBillingController.fill_table_red
    private void fill_table(Table table, boolean is_igst, int serial) {

        int[] NUM_COLS = {9, 9};
        final boolean[] alt_gray = {false, true};
        boolean first_time[] = {true, false};
        size_of_bill = bill.getSize();
        BigDecimal[] total_quant = {BigDecimal.ZERO, BigDecimal.ZERO};
        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            PdfFont f1 = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            int[] skip_count = {serial * NO_OF_ITEMS, 0};
            int[] item_count = {0, 0};

            bill.getItems().forEach(bi -> {
                if (size_of_bill + skip_count[0] == NO_OF_SUMMARY_ITEMS) {
                    return;
                }

                if (--skip_count[0] < 0 && item_count[0]++ < NO_OF_ITEMS) {

//                String mn = bi.getMfgName().length() > 11
//                        ? bi.getMfgName().substring(0, 10) : bi.getMfgName();
//                String pn = bi.getProdName().length() > 19
//                        ? bi.getProdName().substring(0, 18) : bi.getProdName();
                    // any length prod name
                    items_proc++;
                    //    System.out.println("\t\tItems processed = " + items_proc);
                    String pn = bi.getProdName();
                    String pc = bi.getProdCode();

                    if (bi.getSerialNo().equals("Sub Total")) {
//
//                        //Add buffer area
//                        if (size_of_bill < 16) {
//                            Cell cell251 = new Cell(1, is_igst ? NUM_COLS[0] : NUM_COLS[1])
//                                    .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                                    .add("").setMaxHeight(1000)
//                                    .setMinHeight(10 * (16 - size_of_bill));
//                            table.addCell(cell251);
//                            Cell cell251_1 = new Cell(1, is_igst ? NUM_COLS[0] : NUM_COLS[1])
//                                    .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                                    .add("").setMaxHeight(1000)
//                                    .setMinHeight(10 * (16 - size_of_bill));
//                            Cell cell251_2 = new Cell(1, NUM_COLS[0])
//                                    .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                                    .add("")
//                                    .setMinHeight(60);
//
//                            table.addCell(cell251_1)
//                                    .addCell(cell251_2);
//
//                        }
//                        Cell cell252 = new Cell(1, is_igst ? NUM_COLS[0] : NUM_COLS[1])
//                                .setBorderBottom(new SolidBorder(Color.BLACK, 2))
//                                .setBorderLeft(Border.NO_BORDER)
//                                .setBorderRight(Border.NO_BORDER)
//                                .setBorderTop(Border.NO_BORDER)
//                                //.setFontColor(Color.WHITE)
//                                .add("").setMinHeight(2);
//                        table.addCell(cell252);
//                        Cell cell252_1 = new Cell(1, 4)
//                                .setBorder(Border.NO_BORDER)
//                                .setFont(f1).setFontSize(9).setBold()
//                                //.setFontColor(Color.WHITE)
//                                .add(new Paragraph("TOTAL QUANTITY"))
//                                // .setMinHeight(10)
//                                .setTextAlignment(TextAlignment.CENTER);
//                        Cell cell252_2 = new Cell(1, 4)
//                                .setBorder(Border.NO_BORDER)
//                                //                            .setFontColor(Color.WHITE)
//                                .setFont(f1).setFontSize(9).setBold()
//                                .add(new Paragraph(total_quant[0].toString()))
//                                // .setMinHeight(10)
//                                .setTextAlignment(TextAlignment.LEFT);
//                        table.addCell(cell252_1).addCell(cell252_2);

                        //store summary items
                        //    last_items.add(bi);
                    } else if (bi.getSerialNo().equals("Tax")) {

                        //store summary items
                        //  last_items.add(bi);
                    } else if (bi.getSerialNo().equals("Total")) {

                        //store summary items
                        // last_items.add(bi);
                    } else {

                        //add total quant
                        total_quant[0] = total_quant[0].add(BigDecimal.valueOf(
                                Double.parseDouble(bi.getQuantValue())));

                        //Alternate background change
                        if (alt_gray[0]) {
                            alt_gray[0] = false;
                        } else {
                            alt_gray[0] = true;
                        }
                        Color c = alt_gray[0] ? Color.WHITE : new DeviceRgb(244, 244, 244);
                        // Color c = Color.WHITE;
                        Color g = new DeviceRgb(244, 244, 244);

                        Cell csno = new Cell(1, 1).add(bi.getSerialNo()).setFontSize(9).setBorder(Border.NO_BORDER)
                                .setBackgroundColor(c);

//                    Cell cmn = new Cell(1, 1).add(mn).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell cpn = new Cell(1, 1).add(pn).setFontSize(9).setFont(f1).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell cpc = new Cell(1, 1).add(pc).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell chsn = new Cell(1, 1).add(bi.getHsnCode()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell cq = new Cell(1, 1).add(bi.getQuantValue()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.CENTER);
//                    Cell cu = new Cell(1, 1).add(bi.getUnitsValue()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.CENTER);
                        Cell cr = new Cell(1, 1).add(bi.getRateValue()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);
                        Cell ca = new Cell(1, 1).add(bi.getAmountValue()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);

                        if (first_time[0]) {
                            csno.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cpc.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cpn.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            chsn.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cq.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cr.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            ca.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);

                        } else {
                            csno.setBorder(Border.NO_BORDER);
                            cpc.setBorder(Border.NO_BORDER);
                            cpn.setBorder(Border.NO_BORDER);
                            chsn.setBorder(Border.NO_BORDER);
                            cq.setBorder(Border.NO_BORDER);
                            cr.setBorder(Border.NO_BORDER);
                            ca.setBorder(Border.NO_BORDER);
                        }
                        table.addCell(csno);
                        table.addCell(cpc);
                        //    table.addCell(cmn);
                        table.addCell(cpn);
                        table.addCell(chsn);
                        table.addCell(cq);
                        //   table.addCell(cu);
                        table.addCell(cr);
                        table.addCell(ca);
                        if (is_igst) {
                            Cell cigst = new Cell(1, 1).add(bi.getIgst()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);
                            Cell cigstamt = new Cell(1, 1).add(bi.getITaxAmount().toString()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);

                            if (first_time[0]) {
                                cigst.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                                cigstamt.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                                first_time[0] = false;
                            } else {
                                cigst.setBorder(Border.NO_BORDER);
                                cigstamt.setBorder(Border.NO_BORDER);
                            }
                            table.addCell(cigst);
                            table.addCell(cigstamt);
                        } else {
                            Cell cgst = new Cell(1, 1).add(bi.getGst()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);
                            Cell cgstamt = new Cell(1, 1).add(bi.getTaxAmount().toString()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);
//                        Cell csgst = new Cell(1, 1).add(bi.getSgst()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);
//                        Cell csgstamt = new Cell(1, 1).add(bi.getSTaxAmount().toString()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);

                            if (first_time[0]) {
                                cgst.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                                cgstamt.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                                first_time[0] = false;
                            } else {
                                cgst.setBorder(Border.NO_BORDER);
                                cgstamt.setBorder(Border.NO_BORDER);
                            }
                            table.addCell(cgst);
                            table.addCell(cgstamt);
//                        table.addCell(csgst);
//                        table.addCell(csgstamt);

                        }
                    }

                }

            });

            //Add buffer area
            if (item_count[0] < (NO_OF_ITEMS - NO_OF_SUMMARY_ITEMS)) {
                Cell cell251 = new Cell(1, is_igst ? NUM_COLS[0] : NUM_COLS[1])
                        .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
                        .add("").setMaxHeight(1000)
                        .setMinHeight(10 * ((NO_OF_ITEMS - NO_OF_SUMMARY_ITEMS) - item_count[0]));
                table.addCell(cell251);
                Cell cell251_1 = new Cell(1, is_igst ? NUM_COLS[0] : NUM_COLS[1])
                        .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
                        .add("").setMaxHeight(1000)
                        .setMinHeight(10 * ((NO_OF_ITEMS - NO_OF_SUMMARY_ITEMS) - item_count[0]));
//                Cell cell251_2 = new Cell(1, NUM_COLS[0])
//                        .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                        .add("")
//                        .setMinHeight(60);

                table.addCell(cell251_1) //.addCell(cell251_2)
                        ;

            }

            //add total quant + solid line
            Cell cell252 = new Cell(1, is_igst ? NUM_COLS[0] : NUM_COLS[1])
                    .setBorderBottom(new SolidBorder(Color.BLACK, 2))
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)
                    .setBorderTop(Border.NO_BORDER)
                    //.setFontColor(Color.WHITE)
                    .add("").setMinHeight(2);
            table.addCell(cell252);
            Cell cell252_1 = new Cell(1, 4)
                    .setBorder(Border.NO_BORDER)
                    .setFont(f).setFontSize(9).setBold()
                    //.setFontColor(Color.WHITE)
                    .add(new Paragraph("TOTAL QUANTITY"))
                    // .setMinHeight(10)
                    .setTextAlignment(TextAlignment.CENTER);
            Cell cell252_2 = new Cell(1, 4)
                    .setBorder(Border.NO_BORDER)
                    //                            .setFontColor(Color.WHITE)
                    .setFont(f).setFontSize(9).setBold()
                    .add(new Paragraph(total_quant[0].toString()))
                    // .setMinHeight(10)
                    .setTextAlignment(TextAlignment.LEFT);
            table.addCell(cell252_1).addCell(cell252_2);

        } //        catch(BreakException e){
        //        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    Table add_end(boolean is_gst, boolean is_last_page) {
        //add table at the bottom of the bill
        float[] columnWidths = {20, 20, 20, 20, 20, 20, 20, 20};
        Table table_end = new Table(columnWidths);
        table_end.setWidthPercent(100);
        final int END_FONT_SIZE = 7;
        final int END_TOTAL_FONT_SIZE = 9;
        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);

            Cell cell1 = new Cell(5, 5)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) // .setBorder(Border.NO_BORDER)
                    ;
            Cell cell2_1 = new Cell(11, 2)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_TOTAL_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            Cell cell2_2 = new Cell(11, 1)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_TOTAL_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;

            cell1.add(new Paragraph("BANK DETAILS: ").setUnderline())
                    .add("Bank Name: " + bness.getBankName())
                    .add("Branch       : " + bness.getBankBranch())
                    .add("A/C No.      : " + bness.getBankAcNo())
                    .add("IFSC Code : " + bness.getBankIfsc());

            if (is_last_page) {
                last_items.forEach(bi -> {
                    if (bi.getSerialNo().equals("Sub Total")) {

                        cell2_1.add((new Paragraph("Sub Total")).setTextAlignment(TextAlignment.LEFT)).setBorderRight(Border.NO_BORDER);
                        cell2_2.add(new Paragraph(bi.getAmountValue()).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER);

                    } else if (bi.getSerialNo().equals("Tax")) {
                        cell2_1.add(new Paragraph("Tax").setTextAlignment(TextAlignment.LEFT)).setBorderRight(Border.NO_BORDER);
                        cell2_2.add(new Paragraph(bi.getAmountValue()).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER);

                    } else if (bi.getSerialNo().equals("Total")) {
                        cell2_1.add(new Paragraph("Total").setTextAlignment(TextAlignment.LEFT)).setBorderRight(Border.NO_BORDER);
                        cell2_2.add(new Paragraph(bi.getAmountValue()).setTextAlignment(TextAlignment.RIGHT)).setBorderLeft(Border.NO_BORDER);

                    }
                });
            } else {
                cell2_1.add("Total on ").setBorderRight(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.RIGHT);
                cell2_2.add(" last page").setBorderLeft(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.LEFT).setMarginLeft(5).setPaddingLeft(5);

            }
            table_end.addCell(cell1).addCell(cell2_1).addCell(cell2_2);
            Cell cell3_1 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            Cell cell3_2 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            Cell cell3_3 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            Cell cell3_4 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            Cell cell3_5 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            if (igst_chck_bx.isSelected()) {
                table_end.addCell(cell3_1.add("CLASS").setBorderRight(Border.NO_BORDER))
                        .addCell(cell3_2.add("Taxable Amount").setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER))
                        .addCell(cell3_3.add("IGST Amount").setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER))
                        .addCell(cell3_4.add("").setMinWidth(20).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER))
                        .addCell(cell3_5.add(" ").setMinWidth(20).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));
            } else {
                table_end.addCell(cell3_1.add("CLASS").setBorderRight(Border.NO_BORDER))
                        .addCell(cell3_2.add("Taxable Amount").setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER))
                        .addCell(cell3_3.add("SGST Amount").setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER))
                        .addCell(cell3_4.add("CGST Amount").setMinWidth(20).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER))
                        .addCell(cell3_5.add("Total Amount").setMinWidth(20).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));

            }
            //only storing all gst values in sgst
            int max_num = igst_chck_bx.isSelected() ? bness.getIgstList().size()
                    : bness.getCgstList().size();

            BigDecimal[] tot = {BigDecimal.ZERO, BigDecimal.ZERO};
            BigDecimal[] tots = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};

            for (int i = 0; i < max_num; i++) {
                Cell cell3_1_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE)
                        //  .setBold()
                        .setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER) //.setBorder(Border.NO_BORDER)
                        ;
                Cell cell3_2_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE)
                        // .setBold()
                        .setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                Cell cell3_3_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE)
                        // .setBold()
                        .setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                Cell cell3_4_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE)
                        //.setBold()
                        .setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                Cell cell3_5_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);

                if (igst_chck_bx.isSelected()) {
                    BigDecimal[] amt = {BigDecimal.ZERO, BigDecimal.ZERO};
                    getTax4Gst(bness.getIgstList().get(i), 1, amt);
                    table_end.addCell(cell3_1_1.add("GST " + bness.getIgstList().get(i) + "%"))
                            .addCell(cell3_2_1.add(amt[0].toString()))
                            .addCell(cell3_3_1.add(amt[1].toString()))
                            .addCell(cell3_4_1.add("").setMinWidth(20))
                            .addCell(cell3_5_1.add("").setMinWidth(20));
                    tot[0] = tot[0].add(amt[0]);
                    tot[1] = tot[1].add(amt[1]);
                } else {
                    //cgs
                    BigDecimal[] samt = {BigDecimal.ZERO, BigDecimal.ZERO};
                    BigDecimal[] camt = {BigDecimal.ZERO, BigDecimal.ZERO};

                    getTax4Gst(bness.getSgstList().get(i), 3, samt);
                    getTax4Gst(bness.getCgstList().get(i), 2, camt);
//                    System.out.println("DEBUG s% -" + bness.getSgstList().get(i).toString() +
//                            " samt[0] = " + samt[0].toString() + " samt[1] = " + samt[1].toString());
//                    System.out.println("DEBUG c% -" + bness.getCgstList().get(i).toString() +
//                            " camt[0] = " + camt[0].toString() + " camt[1] = " + camt[1].toString());
    
                    //samt[0] and camt[0] will have total in each
                    tots[0] = tots[0].add(samt[0]);
                    tots[1] = tots[1].add(samt[1]);
                    tots[2] = tots[2].add(camt[1]);
//                    table_end.addCell(cell3_1_1.add("GST " + bness.getIgstList().get(i) + "%"))
//                            .addCell(cell3_2_1.add(samt[0].add(camt[0]).toString()))
//                            .addCell(cell3_3_1.add(samt[1].toString()))
//                            .addCell(cell3_4_1.add(camt[1].toString()).setMinWidth(20))
//                            .addCell(cell3_5_1.add(samt[1].add(camt[1]).toString()).setMinWidth(20));
                    table_end.addCell(cell3_1_1.add("GST " + bness.getIgstList().get(i) + "%"))
                            .addCell(cell3_2_1.add(samt[0].toString()))
                            .addCell(cell3_3_1.add(samt[1].toString()))
                            .addCell(cell3_4_1.add(camt[1].toString()).setMinWidth(20))
                            .addCell(cell3_5_1.add(samt[1].add(camt[1]).toString()).setMinWidth(20));
                }
            }
            for (int i = 0; i < 4 - max_num; i++) {
                Cell cell3_1_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER);
                Cell cell3_2_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                Cell cell3_3_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                Cell cell3_4_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                Cell cell3_5_1 = new Cell(1, 1)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.TOP)
                        .setFont(f)
                        .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                        .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
                table_end.addCell(cell3_1_1.add("").setMinHeight(10))
                        .addCell(cell3_2_1.add("").setMinHeight(10))
                        .addCell(cell3_3_1.add("").setMinHeight(10))
                        .addCell(cell3_4_1.add("").setMinHeight(10).setMinWidth(20))
                        .addCell(cell3_5_1.add("").setMinHeight(10).setMinWidth(20));
            }
//for total taxes
            Cell cell3_1_2 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK)
                    .setPadding(0).setBorderRight(Border.NO_BORDER);
            Cell cell3_2_2 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK)
                    .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
            Cell cell3_3_2 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK)
                    .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
            Cell cell3_4_2 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK)
                    .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
            Cell cell3_5_2 = new Cell(1, 1)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK)
                    .setPadding(0).setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
            if (igst_chck_bx.isSelected()) {
                table_end.addCell(cell3_1_2.add("TOTAL "))
                        .addCell(cell3_2_2.add(tot[0].toString()))
                        .addCell(cell3_3_2.add(tot[1].toString()))
                        .addCell(cell3_4_2.add("").setMinWidth(20))
                        .addCell(cell3_5_2.add("").setMinWidth(20));
            } else {
                //cgs
                table_end.addCell(cell3_1_2.add("TOTAL "))
                        .addCell(cell3_2_2.add(tots[0].toString()))
                        .addCell(cell3_3_2.add(tots[1].toString()))
                        .addCell(cell3_4_2.add(tots[2].toString()).setMinWidth(20))
                        .addCell(cell3_5_2.add(tots[1].add(tots[2]).toString())
                                .setMinWidth(20));
            }

            //for total in words
            Cell cell4 = new Cell(1, 8)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;

            //terms and conditions
            Cell cell5 = new Cell(8, 5)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    ;
            //Authorized signatory
            Cell cell6 = new Cell(8, 3)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setFont(f)
                    .setFontSize(END_FONT_SIZE).setBold().setFontColor(Color.BLACK)
                    .setPadding(0) //.setBorder(Border.NO_BORDER)
                    .setMinWidth(80)
                    ;

            //test data
            YourNumberMyWord words = new YourNumberMyWord();
            last_items.forEach(bi -> {

                if (bi.getSerialNo().equals("Total")) {
                    int i1 = (int) Double.parseDouble(bi.getAmountValue());
                    int i = bi.getAmountValue().indexOf(".");

                    String sstr = bi.getAmountValue().substring(i + 1).length() > 2
                            ? bi.getAmountValue().substring(i + 1, i + 3)
                            : bi.getAmountValue().substring(i + 1);
                    int i2 = Integer.parseInt(sstr);
                    String s = (i2 == 0) ? "" : ("and " + words.convert(i2) + " paise");
                    cell4.add(new Paragraph("Rupees" + words.convert(i1)
                            + s));
                }
            });
            cell5.add(new Paragraph("Terms and Conditions: "));
            for (String cond : bness.getTerms()) {
                cell5.add(new Paragraph(cond));
            }
            cell6.add(new Paragraph(" For " + Utils.getCompanyName() + "\n\n\n ").setTextAlignment(TextAlignment.CENTER));
            cell6.add(new Paragraph("Authorized Signatory").setTextAlignment(TextAlignment.CENTER));
            table_end.addCell(cell4).addCell(cell5).addCell(cell6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table_end;
    }

    private void getTax4Gst(double gst, int gst_type, BigDecimal[] amt) {

        bill.getItems().forEach(bi -> {
            if (!(bi.getSerialNo().equals("Sub Total")
                    || bi.getSerialNo().equals("Total")
                    || bi.getSerialNo().equals("Tax"))) {
                switch (gst_type) {
                    case 1:
                        //igst
                        if (gst == Double.valueOf(bi.getIgst())) {
                            amt[0] = amt[0].add(BigDecimal.valueOf(Double.valueOf(bi.getAmountValue())));
                            amt[1] = amt[1].add(bi.getITaxAmount());
                        }
                        break;
                    case 2:
                        //cgst
                        if (gst == Double.valueOf(bi.getCgst())) {
                            amt[0] = amt[0].add(BigDecimal.valueOf(Double.valueOf(bi.getAmountValue())));
                            amt[1] = amt[1].add(bi.getCTaxAmount());
                        }
                        break;
                    case 3:
                        //sgst
                        if (gst == Double.valueOf(bi.getSgst())) {
                            amt[0] = amt[0].add(BigDecimal.valueOf(Double.valueOf(bi.getAmountValue())));
                            amt[1] = amt[1].add(bi.getSTaxAmount());
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void setTrStageState(boolean s) {
        tr_stage_on = s;
    }

    public static void setBTText(String s) {

        str_prop_phone_num.set(s);

    }

    public static void setSTText(String s) {
        str_prop_phone_num1.set(s);
    }

    private void fillAddressData() {
        int sno1, sno2;
        sno1 = sno2 = 0;
        if (Validator.validInt(phone_num.getText())) {
            sno1 = Integer.parseInt(phone_num.getText());
        }
        if (phone_num.getText().length() == 0) {
            bill.tr.setBkTo("");
        }
        try {
            String str = "SELECT *  FROM ADDRESS WHERE id = ?";
            PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(str);
            p_stmt.setInt(1, sno1);
            ResultSet rs = p_stmt.executeQuery();
            if (rs.next()) {
                bill.btCustomer = new Address(
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
                bill.tr.setBkTo(rs.getString("bk"));
            }
            if (Validator.validInt(phone_num1.getText())) {
                sno2 = Integer.parseInt(phone_num1.getText());
            }
            String str1 = "SELECT *  FROM ADDRESS WHERE id = ?";
            PreparedStatement p_stmt1 = Utils.getConnection1().prepareStatement(str1);
            p_stmt1.setInt(1, sno2);
            ResultSet rs1 = p_stmt1.executeQuery();
            if (rs1.next()) {
                //not getting the shipped to cutomer ffrom db
                bill.stCustomer = new Address();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Table add_items_red(int serial) {
        size_of_bill = bill.getSize();
        Color bg = new DeviceRgb(244, 244, 244);
        Cell cell1, cell5, cell3, cell4, cell6, cell7, cell8, cell9, cell12;
        cell1 = cell3 = cell5 = cell4 = cell6
                = cell7 = cell8 = cell9 = cell12 = new Cell(1, 1);
        float[] col = {0f, 0f};
        Table table = new Table(col);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            cell1 = new Cell(1, 1)
                    // .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("SNO"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell5 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Code"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

//            cell3 = new Cell(1, 1)
//                    //   .setBorderLeft(Border.NO_BORDER)
//                    //    .setBorderTop(Border.NO_BORDER)
//                    //   .setBorderRight(Border.NO_BORDER)
//                    .add(new Paragraph("Mfg"))
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                    .setBackgroundColor(bg)
//                    .setFont(f)
//                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell4 = new Cell(1, 1)
                    //    .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Product"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell6 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //    .setBorderTop(Border.NO_BORDER)
                    //   .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("HSN\nCode"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell7 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //  .setBorderTop(Border.NO_BORDER)
                    //    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Quant/\nUnits"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//            cell8 = new Cell(1, 1)
//                    //   .setBorderLeft(Border.NO_BORDER)
//                    //  .setBorderTop(Border.NO_BORDER)
//                    //  .setBorderRight(Border.NO_BORDER)
//                    .add(new Paragraph("Units"))
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                    .setBackgroundColor(bg)
//                    .setFont(f)
//                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            cell9 = new Cell(1, 1)
                    //   .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    //  .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph("Rate"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            cell12 = new Cell(1, 1)
                    //    .setBorderLeft(Border.NO_BORDER)
                    //   .setBorderTop(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)
                    .add(new Paragraph(" Taxable \n Amount"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(bg).setMarginRight(0)
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
            float[] columnWidths = {3, 6, 24, 7, 7, 7, 12};
            table = new Table(columnWidths);
            table.setWidthPercent(100);

            Image itext = new Image(ImageDataFactory.create(getClass()
                    .getResource("/images/" + "rupee.png")))
                    .setWidth(5).setHeight(7);

            table.addHeaderCell(cell1)
                    .addHeaderCell(cell5)
                    //     .addHeaderCell(cell3)
                    .addHeaderCell(cell4)
                    .addHeaderCell(cell6)
                    .addHeaderCell(cell7)
                    //.addHeaderCell(cell8)
                    .addHeaderCell(cell9)
                    .addHeaderCell(cell12);
            // .addHeaderCell(cell11_1_0);
            fill_table_red(table, serial);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return table;

    }

    //DANGER 4 functions need to be modified if you change this
    //The following have same functionality
    // 1) BillingController.fill_table
    // 2) BillingController.fill_table_red  
    // 3) OldBillingController.fill_table
    // 4) OldBillingController.fill_table_red
    private void fill_table_red(Table table, int serial) {
        int[] NUM_COLS = {9, 9};
        final boolean[] alt_gray = {false, true};
        boolean first_time[] = {true, false};

        BigDecimal[] total_quant = {BigDecimal.ZERO, BigDecimal.ZERO};
//        PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            PdfFont f1 = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            int[] skip_count = {serial * NO_OF_ITEMS, 0};
            int[] item_count = {0, 0};

            bill.getItems().forEach(bi -> {
                if (size_of_bill + skip_count[0] == NO_OF_SUMMARY_ITEMS) {
                    return;
                }

                if (--skip_count[0] < 0 && item_count[0]++ < NO_OF_ITEMS) {
                    // any length prod name
                    String pn = bi.getProdName();
                    String pc = bi.getProdCode();

                    if (bi.getSerialNo().equals("Sub Total")) {

                        //Add buffer area
//                        if (size_of_bill < 16) {
//
//                            Cell cell251 = new Cell(1, NUM_COLS[0])
//                                    .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                                    .add("").setMaxHeight(1000)
//                                    .setMinHeight(10 * (16 - size_of_bill));
//                            table.addCell(cell251);
//                            Cell cell251_1 = new Cell(1, NUM_COLS[0])
//                                    .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                                    .add("").setMaxHeight(1000)
//                                    .setMinHeight(10 * (16 - size_of_bill));
//                            Cell cell251_2 = new Cell(1, NUM_COLS[0])
//                                    .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                                    .add("")
//                                    .setMinHeight(60);
//                            table.addCell(cell251_1).addCell(cell251_2);
//
//                        }
//                        Cell cell252 = new Cell(1, NUM_COLS[0])
//                                .setBorderBottom(new SolidBorder(Color.BLACK, 2))
//                                .setBorderLeft(Border.NO_BORDER)
//                                .setBorderRight(Border.NO_BORDER)
//                                .setBorderTop(Border.NO_BORDER)
//                                //.setFontColor(Color.WHITE)
//                                .add("").setMinHeight(2);
//                        table.addCell(cell252);
//                       
//                        Cell cell252_1 = new Cell(1, 4)
//                                .setBorder(Border.NO_BORDER)
//                                .setFont(f1).setFontSize(9).setBold()
//                                //.setFontColor(Color.WHITE)
//                                .add(new Paragraph("TOTAL QUANTITY"))
//                                // .setMinHeight(10)
//                                .setTextAlignment(TextAlignment.CENTER);
//                        Cell cell252_2 = new Cell(1, 4)
//                                .setBorder(Border.NO_BORDER)
//                                //                            .setFontColor(Color.WHITE)
//                                .setFont(f1).setFontSize(9).setBold()
//                                .add(new Paragraph(total_quant[0].toString()))
//                                // .setMinHeight(10)
//                                .setTextAlignment(TextAlignment.LEFT);
//                        table.addCell(cell252_1).addCell(cell252_2);
                        //store summary items
                        //  last_items.add(bi);
                    } else if (bi.getSerialNo().equals("Tax")) {

                        //store summary items
                        // last_items.add(bi);
                    } else if (bi.getSerialNo().equals("Total")) {

                        //store summary items
                        // last_items.add(bi);
                    } else {

                        //add total quant
                        total_quant[0] = total_quant[0].add(BigDecimal.valueOf(
                                Double.parseDouble(bi.getQuantValue())));

                        //Alternate background change
                        if (alt_gray[0]) {
                            alt_gray[0] = false;
                        } else {
                            alt_gray[0] = true;
                        }
                        Color c = alt_gray[0] ? Color.WHITE : new DeviceRgb(244, 244, 244);
                        // Color c = Color.WHITE;
                        Color g = new DeviceRgb(244, 244, 244);

                        Cell csno = new Cell(1, 1).add(bi.getSerialNo()).setFontSize(9).setBorder(Border.NO_BORDER)
                                .setBackgroundColor(c);

//                    Cell cmn = new Cell(1, 1).add(mn).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell cpn = new Cell(1, 1).add(pn).setFontSize(9).setFont(f1).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell cpc = new Cell(1, 1).add(pc).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell chsn = new Cell(1, 1).add(bi.getHsnCode()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.LEFT);
                        Cell cq = new Cell(1, 1).add(bi.getQuantValue()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.CENTER);
//                    Cell cu = new Cell(1, 1).add(bi.getUnitsValue()).setFontSize(9).setBorder(Border.NO_BORDER).setBackgroundColor(c).setTextAlignment(TextAlignment.CENTER);
                        Cell cr = new Cell(1, 1).add(bi.getRateValue()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);
                        Cell ca = new Cell(1, 1).add(bi.getAmountValue()).setFontSize(9).setBackgroundColor(c).setTextAlignment(TextAlignment.RIGHT);

                        if (first_time[0]) {
                            csno.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cpc.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cpn.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            chsn.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cq.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            cr.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            ca.setBorderLeft(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
                            first_time[0] = false;
                        } else {
                            csno.setBorder(Border.NO_BORDER);
                            cpc.setBorder(Border.NO_BORDER);
                            cpn.setBorder(Border.NO_BORDER);
                            chsn.setBorder(Border.NO_BORDER);
                            cq.setBorder(Border.NO_BORDER);
                            cr.setBorder(Border.NO_BORDER);
                            ca.setBorder(Border.NO_BORDER);
                        }
                        table.addCell(csno);
                        table.addCell(cpc);
                        //    table.addCell(cmn);
                        table.addCell(cpn);
                        table.addCell(chsn);
                        table.addCell(cq);
                        //   table.addCell(cu);
                        table.addCell(cr);
                        table.addCell(ca);
                    }

                }
            });

            //Add buffer area
            if (item_count[0] < (NO_OF_ITEMS - NO_OF_SUMMARY_ITEMS)) {
                Cell cell251 = new Cell(1, NUM_COLS[1])
                        .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
                        .add("").setMaxHeight(1000)
                        .setMinHeight(10 * ((NO_OF_ITEMS - NO_OF_SUMMARY_ITEMS) - item_count[0]));
                table.addCell(cell251);
                Cell cell251_1 = new Cell(1, NUM_COLS[1])
                        .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
                        .add("").setMaxHeight(1000)
                        .setMinHeight(10 * ((NO_OF_ITEMS - NO_OF_SUMMARY_ITEMS) - item_count[0]));
//                Cell cell251_2 = new Cell(1, NUM_COLS[0])
//                        .setBorder(Border.NO_BORDER).setFontColor(Color.WHITE)
//                        .add("")
//                        .setMinHeight(60);

                table.addCell(cell251_1) //.addCell(cell251_2)
                        ;

            }

            //add total quant + solid line
            Cell cell252 = new Cell(1, NUM_COLS[1])
                    .setBorderBottom(new SolidBorder(Color.BLACK, 2))
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)
                    .setBorderTop(Border.NO_BORDER)
                    //.setFontColor(Color.WHITE)
                    .add("").setMinHeight(2);
            table.addCell(cell252);
            Cell cell252_1 = new Cell(1, 4)
                    .setBorder(Border.NO_BORDER)
                    .setFont(f).setFontSize(9).setBold()
                    //.setFontColor(Color.WHITE)
                    .add(new Paragraph("TOTAL QUANTITY"))
                    // .setMinHeight(10)
                    .setTextAlignment(TextAlignment.CENTER);
            Cell cell252_2 = new Cell(1, 4)
                    .setBorder(Border.NO_BORDER)
                    //                            .setFontColor(Color.WHITE)
                    .setFont(f).setFontSize(9).setBold()
                    .add(new Paragraph(total_quant[0].toString()))
                    // .setMinHeight(10)
                    .setTextAlignment(TextAlignment.LEFT);
            table.addCell(cell252_1).addCell(cell252_2);

        } //        catch(BreakException e){
        //        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEditableTable() {

    }

    private <BillItem> TableColumn<BillItem, ?> getTableColumnByName(
            TableView<BillItem> tableView, String name) {
        for (TableColumn<BillItem, ?> col : tableView.getColumns()) {
            if (col.getText().equals(name)) {
                return col;
            }
        }
        return null;
    }

    private void setupQuantCol() {
        quant_col.setCellFactory(TextFieldTableCell.forTableColumn());

        // updates the quant_col field on the BillItem object to the
        // committed value
        quant_col.setOnEditCommit(event -> {

            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();

            ((BillItem) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setQuantValue(value);

            refresh_total("");
            billView.refresh();

        });

    }

    private void setupRateCol() {
        rate_col.setCellFactory(TextFieldTableCell.forTableColumn());

        // updates the quant_col field on the BillItem object to the
        // committed value
        rate_col.setOnEditCommit(event -> {

            final String value = event.getNewValue() != null ? event.getNewValue()
                    : event.getOldValue();

            ((BillItem) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setRateValue(value);
            refresh_total("");
            billView.refresh();

        });

    }

    private void refresh_total(String newValue) {
        bill.delItem(total_item_no - 1);
        bill.delItem(tax_item_no - 1);
        bill.delItem(tamount_item_no - 1);

        ArrayList<BillItem> tmpBillItemArray = new ArrayList<>();
        bill.getItems().forEach(bill_item -> {
            bill_item.setRtax(newValue);
            tmpBillItemArray.add(bill_item);
        });

        bill.clear();
        resetItemNoAndTotalItemNo();
        tmpBillItemArray.forEach(tmp_item -> {
            tmp_item.setSerialNo(String.valueOf(item_no));
            addBillItemOnly(tmp_item);

        });
        if (tmpBillItemArray.size() != 0) {
            addTotal();
        }
    }

    private void calcTax(TextField t, String newValue) {
        if ((newValue.length() != 0)
                && !Validator.notValidAmount(newValue)) {
            if (Utils.isRedBilling()) {
                refresh_total(newValue);
            } else {
                refresh_total("");
            }
        }
    }

    private void fillLastItems() {
        int size = bill.getSize();

        int last = size - 3;

        last_items.add(bill.getItem(last));
        last_items.add(bill.getItem(last + 1));
        last_items.add(bill.getItem(last + 2));

    }

    public void createJsonFile(Bill b) {
        SerialBill sbill = new SerialBill();
        sbill.bill_no = b.bill_no;
        sbill.btCustomer = b.btCustomer;
        sbill.date_time = b.date_time;
        sbill.quote_no = b.quote_no;
        sbill.tax = b.tax;
        sbill.tr = new Transport(b.tr);
        b.getItems().forEach(bi -> {
            sbill.addItem(bi);
        });

        Gson gson = new GsonBuilder().create();
        try {
            Writer writer = new FileWriter(Utils.getDbBkpDir() + sbill.getBillNo() + ".bdat");
            gson.toJson(sbill, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
