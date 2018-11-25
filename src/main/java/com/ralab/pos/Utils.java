/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.Alert;

/**
 *
 * @author admin
 */
public class Utils {

    private static Connection db_con;
    private static Connection green_db_con;
    private static Connection red_db_con;
    private static Connection jaas_db_con;

    private static List<String> states
            = Arrays.asList("Andhra Pradesh",
                    "Andaman and Nicobar Islands",
                    "Arunachal Pradesh",
                    "Assam",
                    "Bihar",
                    "Chandigarh",
                    "Chhattisgarh",
                    "Dadra and Nagar Haveli",
                    "Daman and Diu",
                    "Delhi",
                    "Goa",
                    "Gujarat",
                    "Haryana",
                    "Himachal Pradesh",
                    "Jammu and Kashmir",
                    "Jharkhand",
                    "Karnataka",
                    "Kerala",
                    "Lakshadweep",
                    "Madhya Pradesh",
                    "Maharashtra",
                    "Manipur",
                    "Meghalaya",
                    "Mizoram",
                    "Nagaland",
                    "Orissa",
                    "Pondicherry",
                    "Punjab",
                    "Rajasthan",
                    "Sikkim",
                    "Tamil Nadu",
                    "Telangana",
                    "Tripura",
                    "Uttaranchal",
                    "Uttar Pradesh",
                    "West Bengal"
            );

    private static boolean gst_state = true;
    private static boolean green_bill = true;
 private static String companyName = "";
private static String addressLine1 = "";
private static String addressLine2 = "";
private static String phoneNo = "";
private static String gstNo = "";
private static String cityZip = " -3 001";
    private static String imgLogo = "n.png";
    private static String billDir;
    private static String sampleBillDir;
    private static String stockDir;
    private static String repDir;
    private static String dbDir;
    private static String still_not_opensource = "ralabbegins";
    private static String db1 = "trading";
    private static String db2 = "trading_bkp";
    private static String db1_acc = "tractor_sw";
    private static String db1_pwd = "tractor_sw~123";
    private static String db2_acc = "tractor_sw";
    private static String db2_pwd = "tractor_sw~123";
    private static String jaasDb_acc = "billing_sw";
    private static String jaasDb_pwd = "billing_sw~123";
    private static String dbBkpDir;
    private static String jaasDb = "jaasdbt";
    private static String regEmail = null;
    private static String regPwd = null;
    private static String AESKey = still_not_opensource;
    private static String serverLoc = null;
    private static InputStream is = null;

    Utils() {
        try {
           // companyName = AES.decrypt(companyName, still_not_opensource);
          //  addressLine1 = AES.decrypt(addressLine1, still_not_opensource);
         //   addressLine2 = AES.decrypt(addressLine2, still_not_opensource);
         //  phoneNo = AES.decrypt(phoneNo, still_not_opensource);
         //   gstNo = AES.decrypt(gstNo, still_not_opensource);
          is = getClass().getResourceAsStream("/misc/dbloc.txt");
           setServerLoc();

            green_db_con = DriverManager.getConnection(
                    "jdbc:mysql://" + serverLoc + ":3306/" + getDb1(),
                    getDb1_acc(),
                    getDb1_pwd());
            red_db_con = DriverManager.getConnection(
                    "jdbc:mysql://" + serverLoc + ":3306/" + getDb2(),
                    getDb2_acc(),
                    getDb2_pwd());
            jaas_db_con = DriverManager.getConnection(
                    "jdbc:mysql://" + serverLoc + ":3306/" + getJaasDb(),
                    getJaasDb_acc(),
                    getJaasDb_pwd());
            System.out.println("jdbc:sqlite:" + getDbDir() + getDb1());
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException c) {

            }
//            green_db_con = DriverManager.getConnection("jdbc:sqlite:" + getDbDir() + getDb1());
//            red_db_con = DriverManager.getConnection("jdbc:sqlite:" + getDbDir() + getDb2());
//            jaas_db_con = DriverManager.getConnection("jdbc:sqlite:" + getDbDir() + getJaasDb());

            db_con = green_db_con;

            //To make sure that the reg email is available for later calls
            checkReg();
            
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("initDB" + e.getMessage());
            error.showAndWait();
        }
    }

    public static Connection getConnection() {
        return db_con;
    }

    public static Connection getJaasDBCon() {
        return jaas_db_con;
    }

    public static Connection getConnection1() {
        return green_db_con;
    }

    public static Connection getConnection2() {
        return red_db_con;
    }

    public static List<String> getStates() {
        return states;
    }

    public static void enableGST() {
        gst_state = true;
    }

    public static void disableGST() {
        gst_state = false;
    }

    public static boolean isGSTEnabled() {
        return gst_state;
    }

    public static void enableGreenBilling() {
        green_bill = true;
        db_con = green_db_con;
    }

    public static void disableGreenBilling() {
        green_bill = false;
        db_con = red_db_con;
    }
    public static boolean isRedBilling(){
        return db_con == red_db_con;
    }
    public static String getCompanyName() {
        return companyName;
    }

    public static String getAddressLine1() {
        return addressLine1;
    }

    public static String getAddressLine2() {
        return addressLine2;
    }

    public static String getCityZip() {
        return cityZip;
    }

    public static String getPhoneNo() {
        return phoneNo;
    }

    public static String getGstNo() {
        return gstNo;
    }

    public static String getCompanyLogo() {
        return imgLogo;
    }

    public static String getBillDir() {
        return billDir;
    }

    public static void setBillDir(String b) {
        billDir = b;
    }
    public static String getSampleBillDir() {
        return sampleBillDir;
    }

    public static void setSampleBillDir(String b) {
        sampleBillDir = b;
    }
    public static String getStockDir() {
        return stockDir;
    }

    public static void setStockDir(String s) {
        stockDir = s;
    }

    public static String getRepDir() {
        return repDir;
    }

    public static void setRepDir(String r) {
        repDir = r;
    }

    public static String getDbDir() {
        return dbDir;
    }

    public static void setDbDir(String r) {
        dbDir = r;
    }

    public static String getDb1() {
        return db1;
    }

    public static String getDb2() {
        return db2;
    }

    public static String getDb1_acc() {
        return db1_acc;
    }

    public static String getDb1_pwd() {
        return db1_pwd;
    }

    public static String getDb2_acc() {
        return db2_acc;
    }

    public static String getDb2_pwd() {
        return db2_pwd;
    }

    public static String getJaasDb_acc() {
        return jaasDb_acc;
    }

    public static void setJaasDb_acc(String a) {
        jaasDb_acc = a;
    }

    public static String getJaasDb_pwd() {
        return jaasDb_pwd;
    }

    public static void setJaasDb_pwd(String p) {
        jaasDb_pwd = p;
    }

    public static void setDbBkpDir(String s) {
        dbBkpDir = s;
    }

    public static String getDbBkpDir() {
        return dbBkpDir;
    }

    public static String getJaasDb() {
        return jaasDb;
    }

    public static void setJaasDb(String s) {
        jaasDb = s;
    }

    public static String getRegEmail() {
        return regEmail;
    }

    public static void setRegEmail(String e) {
        regEmail = e;
    }

    public static String getRegPwd() {
        return regPwd;
    }

    public static void setRegPwd(String p) {
        regPwd = p;
    }

    public static String getAESKey() {
        return AESKey;
    }

    public static void setAESKey(String a) {
        AESKey = a;
    }

    public static void setServerLoc() {
        char[] bytes = new char[15];

        InputStreamReader isr = new InputStreamReader(is);
        try {
            isr.read(bytes, 0, 15);
            serverLoc = new String(bytes);
        } catch (IOException e) {
            serverLoc = "127.0.0.1";
        }
    }

    public static String getServerLoc() {
        return serverLoc;
    }

    public static boolean checkReg() {
        try {
            String s_str = "SELECT * FROM keytbl ";
            PreparedStatement pstmt = Utils.getJaasDBCon().prepareStatement(s_str);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String key = rs.getString("mykey");
                setRegEmail(rs.getString("userid"));
                setRegPwd(rs.getString("password"));

               // if (key.equals(AES.encrypt(Utils.getCompanyName(),
               //         still_not_opensource))) {

                    return true;
            //    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
