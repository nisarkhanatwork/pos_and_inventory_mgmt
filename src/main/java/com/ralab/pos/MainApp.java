/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.io.File;
import java.io.InputStreamReader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

enum SceneType {
    BILLING, RETURNS, PURCHASE, STOCK_STATUS,
    DAILY_REPORTS, MONTHLY_REPORTS, NONE
};

/**
 *
 * @author admin
 */
public class MainApp extends Application {

    Stage login;
    Stage main_screen;

    @Override
    public void start(Stage stage) throws Exception {
        //init Utils
       
        loadBillDir();
         new Utils();
       // Bill.updateBillNoInDB("", true);
     
        try {
            Login l = new Login();

            l.getStage().setResizable(false);
            l.getStage().showAndWait();
            if (!l.isWindowClosed()) {
                Main m = new Main();

                m.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent e) {
                        MainController.closeAll();
                    }
                });
                m.getStage().showAndWait();

            } else {
                stage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

    public void showMain(Stage stage) {

    }

    private void loadBillDir() {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("dd-MM-yyyy");

        String currentDate = sdf.format(dt);
        char[] bytes = new char[7];
        try {
            InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/misc/bills_location.txt"));
            // isr.read(bytes);

            String OS = System.getProperty("os.name").toLowerCase();
            if (OS.indexOf("win") >= 0) {
                isr.read(bytes, 0, 7);
                Utils.setBillDir(new String(bytes) + "\\bills" + "\\" + currentDate + "\\");
                Utils.setSampleBillDir(new String(bytes) + "\\samplebills" + "\\" + currentDate + "\\");
                Utils.setStockDir(new String(bytes) + "\\stock" + "\\");
                Utils.setRepDir(new String(bytes) + "\\reports" + "\\");
                Utils.setDbBkpDir(new String(bytes) + "\\db_bkp" + "\\");
                Utils.setDbDir(new String(bytes) + "\\db" + "\\");
            } else if (OS.indexOf("nux") >= 0) {
                isr.read(bytes, 0, 7);
                Utils.setBillDir(new String(bytes) + "/bills" + "/" + currentDate + "/");
                Utils.setSampleBillDir(new String(bytes) + "/samplebills" + "/" + currentDate + "/");
                Utils.setStockDir(new String(bytes) + "/stock" + "/");
                Utils.setRepDir(new String(bytes) + "/reports" + "/");
                Utils.setDbBkpDir(new String(bytes) + "/db_bkp" + "/");
                Utils.setDbDir(new String(bytes) + "/db" + "/");

            }
            new File(Utils.getBillDir()).mkdirs();
             new File(Utils.getSampleBillDir()).mkdirs();
           new File(Utils.getStockDir()).mkdirs();
            new File(Utils.getRepDir()).mkdirs();
            new File(Utils.getDbBkpDir()).mkdirs();
            new File(Utils.getDbDir()).mkdirs();

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("loadBillDir " + Utils.getBillDir() + " " + e.getMessage());
            error.showAndWait();

        }

    }

}
