/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import static com.ralab.pos.BillingController.bill;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author admin
 */
public class MainController {

    boolean main_stage_on = false;
    boolean cust_stage_on = false;
    boolean ret_stage_on = false;
    boolean pur_stage_on = false;
    boolean rep_stage_on = false;
    boolean addprod_stage_on = false;
    boolean reg_stage_on = false;
    static boolean ab_stage_on = false;
    static boolean ld_stage_on = false;
    static boolean admin_stage_on = false;
    static boolean cd_stage_on = false;

    static Stage pur_stage;
    static Stage ret_stage;
    static Stage main_stage;
    static Stage cust_stage;
    static Stage rep_stage;
    static Stage addprod_stage;
    static Stage reg_stage;
    static Stage ab_stage;
    static Stage ld_stage;
    static Stage admin_stage;
    static Stage cd_stage;

    @FXML
    private Button billing_btn;
    @FXML
    private Button returns_btn;
    @FXML
    private Button add_cust_btn;
    @FXML
    private Button add_prod_btn;
    @FXML
    private Button pur_btn;
    @FXML
    private Button bkp_btn;
    @FXML
    private Button rep_btn;
    @FXML
    private Button stk_stat_btn;
    @FXML
    private Button prod_code_btn;
    @FXML
    private Button admin_btn;
    @FXML
    private Button load_data_btn;
    @FXML
    private Button comp_details_btn;

    static ObservableList<Address> ab_data = FXCollections.observableArrayList( //            new BillItem( "1", "Tyre" , "Smih", "jom", "12", "pcs", "1240", "100")
            );

    @FXML
    public void initialize() {
        if (!Utils.checkReg()) {
            disable_all_btns();
        }
        if (MyBusiness.getUserCred().equals("0")) {
            admin_btn.setVisible(false);
        }
    }

    @FXML
    protected void handleBILLINGButtonAction(ActionEvent event) {
        if (!main_stage_on) {
            showBilling();
            main_stage_on = true;
        } else {
            main_stage.toFront();
        }
    }

    @FXML
    protected void handleRETURNSButtonAction(ActionEvent event) {
        if (!ret_stage_on) {
            showReturns();
            ret_stage_on = true;
        } else {
            ret_stage.toFront();
        }
    }

    @FXML
    protected void handlePURCHASEButtonAction(ActionEvent event) {
        if (!pur_stage_on) {
            showPurchase();
            pur_stage_on = true;
        } else {
            pur_stage.toFront();
        }
    }

    @FXML
    protected void handleADDPRODUCTButtonAction(ActionEvent event) {
        if (!addprod_stage_on) {
            showAddProd();
            addprod_stage_on = true;
        } else {
            addprod_stage.toFront();
        }
    }

    @FXML
    protected void handleSTOCKButtonAction(ActionEvent event) {
        printToPdf();
    }

    @FXML
    protected void handlePRODCODEButtonAction(ActionEvent event) {
        createProdCodePdf();
    }

    @FXML
    protected void handleREPORTSButtonAction(ActionEvent event) {
        if (!rep_stage_on) {
            showReports();
            rep_stage_on = true;
        } else {
            rep_stage.toFront();
        }
    }

    @FXML
    protected void handleBACKUPButtonAction(ActionEvent event) {
//        java.util.Date dt = new Date();
//        java.sql.Date sqldt = new java.sql.Date(dt.getTime());
//        try {
//            String p_str = "SELECT backup_done FROM bill_no WHERE date = ?";
//            PreparedStatement pstmt = Utils.getConnection().prepareStatement(p_str);
//            pstmt.setDate(1, sqldt);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                if (rs.getBoolean("backup_done")) {
//                    System.out.println("\t\tBackup already done...");
//
//                } else {
//
//                    backupDB();
//
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        backupDB();
    }

    @FXML
    protected void handleADMINButtonAction(ActionEvent event) {
        if (!admin_stage_on) {
            showAdmin();
            admin_stage_on = true;
        } else {
            admin_stage.toFront();
        }
    }

    @FXML
    protected void handleLOADDATAButtonAction(ActionEvent event) {
        if (!ld_stage_on) {
            showLoadData();
            ld_stage_on = true;
        } else {
            ld_stage.toFront();
        }
    }

    @FXML
    protected void handleCOMPDETAILSButtonAction(ActionEvent event) {
        if (!cd_stage_on) {
            showCompDetails();
            cd_stage_on = true;
        } else {
            cd_stage.toFront();
        }
    }

    @FXML
    protected void handleAddCustomerButtonAction(ActionEvent event) {
        if (!ab_stage_on) {

            showAddressBook();
            ab_stage_on = true;
        } else {
            ab_stage.toFront();
        }
    }

    public void showBilling() {
        main_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Billing.fxml"));
            Scene scene = new Scene(root, 1100, 650);

            main_stage.setTitle(Utils.getCompanyName() + " Software");
            main_stage.setScene(scene);
            main_stage.setResizable(false);
            main_stage.show();
            if (Utils.isRedBilling()) {
                main_stage.getScene().lookup("#title").setStyle("-fx-font:  25px Helvetica;-fx-fill: red;");
                ((TextField) (main_stage.getScene().lookup("#bill_no")))
                        .setText(bill.getBillNo());
                main_stage.getScene().lookup("#tax").setVisible(true);
                main_stage.
                        getScene().lookup("#tax_label").setVisible(true);
            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showBilling " + e.getMessage());

            error.showAndWait();
        }
        main_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                main_stage_on = false;
            }

        });

        main_stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final private KeyCombination CTRL_SHIFT_UP = new KeyCodeCombination(KeyCode.UP,
                    KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN);
            final private KeyCombination CTRL_SHIFT_DOWN = new KeyCodeCombination(KeyCode.DOWN,
                    KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN);

            final private KeyCombination CTRL_SHIFT_RIGHT = new KeyCodeCombination(KeyCode.RIGHT,
                    KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN);
            final private KeyCombination CTRL_SHIFT_LEFT = new KeyCodeCombination(KeyCode.LEFT,
                    KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN);

            //for combobox
            final private KeyCombination CTRL_DOWN = new KeyCodeCombination(KeyCode.DOWN,
                    KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if ((CTRL_SHIFT_UP.match(ke)
                        || CTRL_SHIFT_DOWN.match(ke)
                        || CTRL_SHIFT_LEFT.match(ke)
                        || CTRL_SHIFT_RIGHT.match(ke))
                        && !bill.getItems()
                                .isEmpty()) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setContentText("Operation Failed ! Please start with EMPTY bill ");
                    error.showAndWait();
                    return;
                }
                if (CTRL_DOWN.match(ke)) {
                    if (ke.getTarget() instanceof ComboBox) {
                        if (ke.getTarget().equals(main_stage.getScene().lookup("#mfg"))) {
                            KeyEvent ke1 = new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false,
                                    false, false, false);
                            KeyCombination SHIFT_DOWN = new KeyCodeCombination(KeyCode.DOWN,
                                    KeyCombination.SHIFT_DOWN);
                            KeyCombination ALT_DOWN = new KeyCodeCombination(KeyCode.DOWN,
                                    KeyCombination.ALT_DOWN);

                            main_stage.getScene().lookup("#mfg").fireEvent(ke1);

                        }

                    }
                }
                if (CTRL_SHIFT_UP.match(ke)) {

//                    Utils.enableGST();
//                    ke.consume(); // <-- stops passing the event to next node
//                    Alert error = new Alert(Alert.AlertType.INFORMATION);
//                    error.setContentText("Enable ");
//                    error.showAndWait();
                } else if (CTRL_SHIFT_DOWN.match(ke)) {
//                    Utils.disableGST();
//                    Alert error = new Alert(Alert.AlertType.INFORMATION);
//                    error.setContentText("Disable ");
//                    error.showAndWait();
//
                } else if (CTRL_SHIFT_LEFT.match(ke)) {
                    Utils.disableGreenBilling();
                    Alert error = new Alert(Alert.AlertType.INFORMATION);
                    error.setContentText("RED ");
                    error.showAndWait();
                    main_stage.getScene().lookup("#title").setStyle("-fx-font:  25px Helvetica;-fx-fill: red;");
                    ((TextField) (main_stage.getScene().lookup("#bill_no")))
                            .setText(bill.getBillNo());
                    main_stage.getScene().lookup("#tax").setVisible(true);
                    main_stage.getScene().lookup("#tax_label").setVisible(true);
                } else if (CTRL_SHIFT_RIGHT.match(ke)) {
                    Utils.enableGreenBilling();
                    Alert error = new Alert(Alert.AlertType.INFORMATION);
                    error.setContentText("GREEN ");
                    error.showAndWait();
                    main_stage.getScene().lookup("#title").setStyle("-fx-font:  25px Helvetica;-fx-fill: green;");
                    ((TextField) (main_stage.getScene().lookup("#bill_no")))
                            .setText(bill.getBillNo());
                    main_stage.getScene().lookup("#tax").setVisible(false);
                    main_stage.getScene().lookup("#tax_label").setVisible(false);

                }
            }
        });
    }

    public void showReturns() {

        ret_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/OldBilling.fxml"));
            Scene scene = new Scene(root, 1100, 650);

            ret_stage.setTitle(Utils.getCompanyName() + " Software");
            ret_stage.setScene(scene);
            ret_stage.setResizable(false);
            ret_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
        ret_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                ret_stage_on = false;
            }

        });

    }

    public void showPurchase() {

        pur_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Purchase.fxml"));
            Scene scene = new Scene(root, 600, 535);

            pur_stage.setTitle(Utils.getCompanyName() + " Software");
            pur_stage.setScene(scene);
            pur_stage.setResizable(false);
            pur_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showPurchase " + e.getMessage());
            error.showAndWait();
        }
        pur_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                pur_stage_on = false;
            }

        });

    }

    public void showReports() {

        rep_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Reports.fxml"));
            Scene scene = new Scene(root, 600, 535);

            rep_stage.setTitle(Utils.getCompanyName() + " Software");
            rep_stage.setScene(scene);
            rep_stage.setResizable(false);
            rep_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showReports " + e.getMessage());
            error.showAndWait();
        }
        rep_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                rep_stage_on = false;
            }

        });

    }

    public void showAddProd() {

        addprod_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddProd.fxml"));
            Scene scene = new Scene(root, 600, 600);

            addprod_stage.setTitle(Utils.getCompanyName() + " Software");
            addprod_stage.setScene(scene);
            addprod_stage.setResizable(false);
            addprod_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showAddProd " + e.getMessage());
            error.showAndWait();
        }
        addprod_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                addprod_stage_on = false;
            }

        });

    }

    public void showRegister() {

        reg_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Register.fxml"));
            Scene scene = new Scene(root, 600, 400);

            reg_stage.setTitle(Utils.getCompanyName() + " Software");
            reg_stage.setScene(scene);
            reg_stage.setResizable(false);
            reg_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showRegister " + e.getMessage());
            error.showAndWait();
        }
        reg_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                reg_stage_on = false;
            }

        });

    }

    public void showAdmin() {

        admin_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin.fxml"));
            Scene scene = new Scene(root, 1100, 500);

            admin_stage.setTitle(Utils.getCompanyName() + " Software");
            admin_stage.setScene(scene);
            admin_stage.setResizable(false);
            admin_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showRegister " + e.getMessage());
            error.showAndWait();
        }
        admin_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                admin_stage_on = false;
            }

        });

    }

    public void showAddressBook() {
        ab_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddressBook.fxml"));
            Scene scene = new Scene(root, 1100, 500);

            ab_stage.setTitle(Utils.getCompanyName() + " Software");
            ab_stage.setScene(scene);
            ab_stage.setResizable(false);
            ab_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("showAddressBook " + e.getMessage());
            error.showAndWait();
        }
        ab_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {

                ab_stage_on = false;

            }

        });

    }

    public void showLoadData() {

        ld_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoadData.fxml"));
            Scene scene = new Scene(root, 600, 600);

            ld_stage.setTitle(Utils.getCompanyName() + " Software");
            ld_stage.setScene(scene);
            ld_stage.setResizable(false);
            ld_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
        ld_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                ld_stage_on = false;
            }

        });

    }

    public void showCompDetails() {

        cd_stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/CompDetails.fxml"));
            Scene scene = new Scene(root, 1100, 500);

            cd_stage.setTitle(Utils.getCompanyName() + " Software");
            cd_stage.setScene(scene);
            cd_stage.setResizable(false);
            cd_stage.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText(e.getMessage());
            error.showAndWait();
        }
        cd_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                cd_stage_on = false;
            }

        });

    }

    public static void closeAll() {
        if (main_stage != null) {
            main_stage.close();
        }
        if (ret_stage != null) {
            ret_stage.close();
        }
        if (cust_stage != null) {
            cust_stage.close();
        }
        if (pur_stage != null) {
            pur_stage.close();
        }
        if (rep_stage != null) {
            rep_stage.close();
        }
        if (addprod_stage != null) {
            addprod_stage.close();
        }
        if (reg_stage != null) {
            reg_stage.close();
        }
        if (ab_stage != null) {
            ab_stage.close();
        }
        if (ld_stage != null) {
            ld_stage.close();
        }
        if (cd_stage != null) {
            cd_stage.close();
        }
        if (admin_stage != null) {
            admin_stage.close();
        }
    }

    private void printToPdf() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd-MM-yyyy_HH_mm_ss");
        String currentDate = sdf.format(dt);

        int size_of_bill = 10;
        // String dest = fileName + ".pdf";
        String dest = Utils.getStockDir() + "Stock_Report_" + currentDate + ".pdf";
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

            //For page header
            VariableHeaderEventHandler handler = new VariableHeaderEventHandler(false, false, null, 3);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            Document doc = new Document(pdfDoc);
            doc.setMargins(20, 20, 20, 20);

            //For page header
            handler.setHeader(Utils.getCompanyName()
                    + String.format(" stock report (%s) ", currentDate));

            Rectangle[] areas = new Rectangle[]{
                //             
                //  new Rectangle(20, 700, 540, 55),
                new Rectangle(20, 20, 540, 715)};

            // for canvas usage one should create a page
            pdfDoc.addNewPage();
            for (Rectangle rect : areas) {
                new PdfCanvas(pdfDoc
                        .getFirstPage())
                        .setLineWidth(0.5f)
                        .setStrokeColor(Color.WHITE)
                        .rectangle(rect).stroke();
            }
            doc.setRenderer(new ColumnDocumentRenderer(doc, areas));

            doc.add(add_items());
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Table add_items() {
        float[] columnWidths = {3, 20, 24, 7, 7, 7};
        Table table = new Table(columnWidths);
        table.setWidthPercent(100);
        table.setFontSize(9);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell1 = new Cell(1, 1)
                    .add(new Paragraph("SNO"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);

            Cell cell3 = new Cell(1, 1)
                    .add(new Paragraph("Mfg"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell4 = new Cell(1, 1)
                    .add(new Paragraph("Prod"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell5 = new Cell(1, 1)
                    .add(new Paragraph("Code"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell6 = new Cell(1, 1)
                    .add(new Paragraph("Quant"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell7 = new Cell(1, 1)
                    .add(new Paragraph("Low Warn"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);

            table.addHeaderCell(cell1)
                    .addHeaderCell(cell3)
                    .addHeaderCell(cell4)
                    .addHeaderCell(cell5)
                    .addHeaderCell(cell6)
                    .addHeaderCell(cell7);
            try {
                Statement stmt = Utils.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM products INNER JOIN"
                        + " (SELECT (CASE WHEN l.m <= p.low_warn THEN 1 ELSE 0 END)"
                        + " AS is_low, p.low_warn, p.prod_code, l.m FROM products p"
                        + " INNER JOIN (SELECT (t.q - s.q) AS m,t.code FROM "
                        + "(SELECT SUM(b.quant) AS q, b.prod_code AS code FROM bills b "
                        + "GROUP BY b.prod_code) AS s INNER JOIN (SELECT SUM(p.quant) "
                        + "AS q, p.prod_code AS code FROM purchase p GROUP BY p.prod_code) "
                        + "AS t ON t.code = s.code ) AS l on l.code = p.prod_code) AS k "
                        + "ON products.prod_code=k.prod_code "
                        + " WHERE is_low = 1 AND products.prod_code <> ''");
                int s_no = 1;
                while (rs.next()) {

                    String mfg = rs.getString("mfg");
                    String prod = rs.getString("prod");
                    String code = rs.getString("prod_code");

                    String quant = rs.getString("m");
                    String low_warn = rs.getString("low_warn");
                    table.addCell(Integer.toString(s_no));

                    table.addCell(mfg);
                    table.addCell(prod);
                    table.addCell(code);
                    table.addCell(quant);
                    table.addCell(low_warn);
                    s_no++;
                }
            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("add_items " + e.getMessage());
                error.showAndWait();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    private void createProdCodePdf() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(dt);

        int size_of_bill = 10;
        // String dest = fileName + ".pdf";
        String dest = Utils.getStockDir() + "Prod_Code_" + currentDate + ".pdf";
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

            //For page header
            VariableHeaderEventHandler handler = new VariableHeaderEventHandler(
                    false, false, null, 3);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            Document doc = new Document(pdfDoc);
            doc.setMargins(20, 20, 20, 20);

            //For page header
            handler.setHeader(Utils.getCompanyName()
                    + String.format(" Product Code (%s) ", currentDate));

            Rectangle[] areas = new Rectangle[]{
                //             
                //  new Rectangle(20, 700, 540, 55),
                new Rectangle(20, 20, 540, 715)};

            // for canvas usage one should create a page
            pdfDoc.addNewPage();
            for (Rectangle rect : areas) {
                new PdfCanvas(pdfDoc
                        .getFirstPage())
                        .setLineWidth(0.5f)
                        .setStrokeColor(Color.WHITE)
                        .rectangle(rect).stroke();
            }
            doc.setRenderer(new ColumnDocumentRenderer(doc, areas));

            doc.add(add_prod_list());
            doc.close();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("createProdCodePdf " + e.getMessage());
            error.showAndWait();
            e.printStackTrace();
        }

    }

    private Table add_prod_list() {
        float[] columnWidths = {3, 11, 24, 7, 7};
        Table table = new Table(columnWidths);
        table.setWidthPercent(100);
        table.setFontSize(9);

        try {
            PdfFont f = PdfFontFactory.createFont(FontConstants.HELVETICA);
            Cell cell1 = new Cell(1, 1)
                    .add(new Paragraph("SNO"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);;

//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell3 = new Cell(1, 1)
                    .add(new Paragraph("Mfg"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell4 = new Cell(1, 1)
                    .add(new Paragraph("Prod"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell5 = new Cell(1, 1)
                    .add(new Paragraph("Code"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);
            Cell cell6 = new Cell(1, 1)
                    .add(new Paragraph("HSNCODE"))
                    .setFont(f)
                    .setFontSize(9).setBold().setFontColor(Color.BLACK);
//                .setFontColor(DeviceGray.WHITE)
//                .setBackgroundColor(DeviceGray.BLACK)
//                .setTextAlignment(TextAlignment.CENTER);

            table.addHeaderCell(cell1)
                    .addHeaderCell(cell3)
                    .addHeaderCell(cell4)
                    .addHeaderCell(cell5)
                    .addHeaderCell(cell6);
            try {
                Statement stmt = Utils.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM products  ORDER BY"
                        + " prod_code ");
                int s_no = 1;
                while (rs.next()) {

                    String mfg = rs.getString("mfg");
                    String prod = rs.getString("prod");
                    String code = rs.getString("prod_code");

                    String hsn_code = rs.getString("hsn_code");
                    table.addCell(Integer.toString(s_no));

                    table.addCell(mfg);
                    table.addCell(prod == null ? "" : prod);
                    table.addCell(code == null ? "" : code);
                    table.addCell(hsn_code == null ? "" : hsn_code);

                    s_no++;

                }
            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("add_prod_list " + e.getMessage());
                error.showAndWait();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public void backupDB() {
        Date dt = new java.util.Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(dt);
        String OS = System.getProperty("os.name").toLowerCase();
        String comp = Utils.getCompanyName().replace(' ', '_');
        String file = Utils.getDbBkpDir() + "db_bkp_" + comp
                + "_" + currentDate + ".sql";
        String executeCmd1 = "mysqldump -u "
                + Utils.getDb1_acc() + " -p" + Utils.getDb1_pwd()
                + " -B " + Utils.getDb1() + " " + Utils.getDb2() + " "
                + Utils.getJaasDb() + " " + " -r " + file;

        String executeCmd2_win = "7z a " + file + ".zip" + " " + file;
        String executeCmd2_lin = "zip -r " + file + ".zip" + " " + file;

        if (OS.indexOf("win") >= 0) {
            Process runtimeProcess;

            try {
                runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", executeCmd1});

                int processComplete = runtimeProcess.waitFor();

                System.out.println(processComplete);

                if (processComplete == 0) {
                    System.out.println("Backup Created Successfully !");
                } else {
                    System.out.println("Couldn't Create the backup !");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {

                runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", executeCmd2_win});

                int processComplete = runtimeProcess.waitFor();

                System.out.println(processComplete);

                if (processComplete == 0) {
                    System.out.println("Compressed Successfully !");
                } else {
                    System.out.println("Couldn't compress backup file !");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            try {

                Runtime runtime = Runtime.getRuntime();
                Process p = runtime.exec(executeCmd1);

                int processComplete = p.waitFor();

                if (processComplete == 0) {

                    System.out.println("Backup created successfully!");

                } else {
                    System.out.println("Could not create the backup");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                Runtime runtime = Runtime.getRuntime();
                Process p = runtime.exec(executeCmd2_lin);

                int processComplete = p.waitFor();

                if (processComplete == 0) {

                    System.out.println("Compressed Successfully !");

                } else {
                    System.out.println("Couldn't compress backup file !");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        ExecutorService es1 = Executors.newCachedThreadPool();
//
//        SendFileEmail semail = new SendFileEmail(Utils.getRegEmail(),
//                Utils.getRegEmail(), file + ".zip");
//        es1.execute(semail);
//        es1.shutdown();
    }

    private void disable_all_btns() {
        billing_btn.setDisable(true);
        returns_btn.setDisable(true);
        add_cust_btn.setDisable(true);
        add_prod_btn.setDisable(true);
        pur_btn.setDisable(true);
        bkp_btn.setDisable(true);
        rep_btn.setDisable(true);
        stk_stat_btn.setDisable(true);
        prod_code_btn.setDisable(true);
    }
}
