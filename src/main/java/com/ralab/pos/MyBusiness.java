/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lab149
 */
public class MyBusiness {

    private static BankDetails bd;
    private static List<Double> igst = new ArrayList<Double>();
    private static List<Double> cgst = new ArrayList<Double>();
    private static List<Double> sgst = new ArrayList<Double>();

    private static List<String> terms = new ArrayList<String>();
    private static String userCred;

    private static int single = 0;

    public MyBusiness() {
        
        if (single == 0) {
            bd = new BankDetails();
            fillGst();
            fillTerms();
            single++;
        }
    }

    public static List<Double> getIgstList() {
        return igst;
    }

    public static List<Double> getSgstList() {
        return sgst;
    }

    public static List<Double> getCgstList() {
        return cgst;
    }

    public static void setBankName(String n) {
        bd.name = n;
    }

    public static String getBankName() {
        return bd.name;
    }

    public static void setBankBranch(String b) {
        bd.branch = b;
    }

    public static String getBankBranch() {
        return bd.branch;
    }

    public static void setBankAcNo(String a) {
        bd.acNo = a;
    }

    public static String getBankAcNo() {
        return bd.acNo;
    }

    public static void setBankIfsc(String i) {
        bd.ifsc = i;
    }

    public static String getBankIfsc() {
        return bd.ifsc;
    }

    public static List<String> getTerms() {
        return terms;
    }

    public static void setUserCred(String updateperm) {
        userCred = updateperm;
    }

    public static String getUserCred() {
        return userCred;
    }

    private static void fillGst() {

        try {
            String p_str = "SELECT * FROM gst ORDER BY percent ASC";
            PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(p_str);
            ResultSet rs = p_stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("type").equals("igst")) {
                    igst.add(rs.getDouble("percent"));
                } else if (rs.getString("type").equals("sgst")) {
                    sgst.add(rs.getDouble("percent"));
                } else if (rs.getString("type").equals("cgst")) {
                    cgst.add(rs.getDouble("percent"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void fillTerms() {
        try {
            String p_str = "SELECT * FROM terms";
            PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(p_str);
            ResultSet rs = p_stmt.executeQuery();

            while (rs.next()) {
                terms.add(rs.getString("cond"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class BankDetails {

        String name;
        String branch;
        String acNo;
        String ifsc;

        public BankDetails() {

            try {
                String p_str = "SELECT * FROM bank_details";
                PreparedStatement p_stmt = Utils.getConnection1().prepareStatement(p_str);
                ResultSet rs = p_stmt.executeQuery();
                if (rs.next()) {
                    name = rs.getString("name");
                    branch = rs.getString("branch");
                    acNo = rs.getString("ac_no");
                    ifsc = rs.getString("ifsc");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
