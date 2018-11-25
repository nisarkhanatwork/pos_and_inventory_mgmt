/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author lab149
 */
public class OldBill {

    static Transport tr;

    static ObservableList<BillItem> items = FXCollections.observableArrayList();

    private OldBill() {
        tr = new Transport("", "", "", "", "", "", "");
    }

    public static void clearTransport() {
        tr.setTrName("");
        tr.setLrNo("");
        tr.setLrDate("");
        tr.setBkTo("");
        tr.setWeight("");
        tr.setCases("");
        tr.setPvtMark("");

    }

    public static void addItem(int i, BillItem bi) {
        items.add(i, bi);
    }

    public static void addItem(BillItem bi) {
        items.add(bi);
    }

    public static void delItem(int i) {
        items.remove(i);
    }

    public static BillItem getItem(int i) {
        return items.get(i);
    }

    public static void clear() {
        items.clear();
    }

    public static int getSize() {
        return items.size();
    }

    public class Transport {

        private String trName;
        private String lrNo;
        private String lrDate;
        private String bkTo;
        private String weight;
        private String cases;
        private String pvtMark;

        public Transport(
                String t,
                String n,
                String d,
                String b,
                String w,
                String c,
                String p) {
            setTrName(t);
            setLrNo(n);
            setLrDate(d);
            setBkTo(b);
            setWeight(w);
            setCases(c);
            setPvtMark(p);

        }

        public void setTrName(String t) {
            trName = t;

        }

        public String getTrName() {
            return trName;
        }

        public void setLrNo(String l) {
            lrNo = l;
        }

        public String getLrNo() {
            return lrNo;
        }

        public void setLrDate(String d) {
            lrDate = d;
        }

        public String getLrDate() {
            return lrDate;
        }

        public void setBkTo(String b) {
            bkTo = b;
        }

        public String getBkTo() {
            return bkTo;
        }

        public void setWeight(String w) {
            weight = w;
        }

        public String getWeight() {
            return weight;
        }

        public void setCases(String c) {
            cases = c;
        }

        public String getCases() {
            return cases;
        }

        public void setPvtMark(String p) {
            pvtMark = p;
        }

        public String getPvtMark() {
            return pvtMark;
        }
    }

}
