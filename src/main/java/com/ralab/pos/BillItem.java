/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.text.Text;
import javafx.beans.property.ReadOnlyStringProperty;
import java.lang.String;
import javafx.scene.control.Alert;

/**
 *
 * @author admin
 */
public class BillItem {
//    private final SimpleStringProperty typeName = new SimpleStringProperty("");
//    private final SimpleStringProperty mfgName = new SimpleStringProperty("");
//    private final SimpleStringProperty prodName = new SimpleStringProperty("");
//    
//    private final SimpleStringProperty quantValue = new SimpleStringProperty(BigDecimal.ZERO.toString());
//    private final SimpleStringProperty unitsValue = new SimpleStringProperty(BigDecimal.ZERO.toString());
//    private final SimpleStringProperty rateValue = new SimpleStringProperty(BigDecimal.ZERO.toString());
//    private final SimpleStringProperty amountValue = new SimpleStringProperty(BigDecimal.ZERO.toString());

    private final SimpleStringProperty serialNo = new SimpleStringProperty("0");

    private final SimpleStringProperty mfgName = new SimpleStringProperty("");
    private final SimpleStringProperty prodName = new SimpleStringProperty("");
    private final SimpleStringProperty quantValue = new SimpleStringProperty(
            BigDecimal.ZERO.toString());
    private final SimpleStringProperty unitsValue = new SimpleStringProperty("");
    private final SimpleStringProperty rateValue = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private final SimpleStringProperty prodCode = new SimpleStringProperty("");
    private final SimpleStringProperty hsnCode = new SimpleStringProperty("");
    private final SimpleStringProperty cgst = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private final SimpleStringProperty sgst = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private final SimpleStringProperty igst = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private final SimpleStringProperty rtax = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private final SimpleStringProperty amountValue = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private static final SimpleStringProperty totalString = new SimpleStringProperty("Total");
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal iTaxAmount = BigDecimal.ZERO;
    private BigDecimal sTaxAmount = BigDecimal.ZERO;
    private BigDecimal cTaxAmount = BigDecimal.ZERO;
    private final SimpleStringProperty gst = new SimpleStringProperty(BigDecimal.ZERO.toString());

    public BillItem() {
        this("", "", "", "0", "", "0", "", "", "", "", "", "", "");
    }

    public BillItem(String serialNo,
            String mfgName,
            String prodName,
            String quantValue,
            String unitsValue,
            String rateValue,
            String prodCode,
            String hsnCode,
            String cgst,
            String sgst,
            String igst,
            String rtax,
            String amountValue
    ) {

        setSerialNo(serialNo);

        setMfgName(mfgName);
        setProdName(prodName);
        setQuantValue(quantValue);
        setUnitsValue(unitsValue);
        setRateValue(rateValue);
        setProdCode(prodCode);
        setHsnCode(hsnCode);
        setCgst(cgst);
        setSgst(sgst);
        setIgst(igst);
        setRtax(rtax);
        setAmountValue(amountValue);
        setOtherValues();
        calculateAmount();

    }

    public String getSerialNo() {
        return serialNo.get();
    }

    public void setSerialNo(String sNo) {

        serialNo.set(sNo);
    }

    public void resetSerialNo(String sNo) {
        serialNo.set("0");
    }

    public String getMfgName() {
        return mfgName.get();
    }

    public void setMfgName(String mName) {
        mfgName.set(mName);
    }

    public String getProdName() {
        return prodName.get();
    }

    public void setProdName(String pName) {
        prodName.set(pName);
    }

    public String getQuantValue() {
        return quantValue.get();
    }

    public void setQuantValue(String qValue) {
        quantValue.set(qValue);
        calculateAmount();
    }

    public String getUnitsValue() {
        return unitsValue.get();
    }

    public void setUnitsValue(String uValue) {
        unitsValue.set(uValue);

    }

    public String getRateValue() {
        return rateValue.get();
    }

    public void setRateValue(String rValue) {
        rateValue.set(rValue);
        calculateAmount();

    }

    public String getProdCode() {
        return prodCode.get();
    }

    public void setProdCode(String pCode) {
        prodCode.set(pCode);
    }

    public String getHsnCode() {
        return hsnCode.get();
    }

    public void setHsnCode(String h) {
        hsnCode.set(h);
    }

    public String getCgst() {
        return cgst.get();
    }

    public void setCgst(String c) {
        cgst.set(c);
        setOtherValues();
    }

    public String getSgst() {

        return sgst.get();

    }

    public void setSgst(String s) {
        sgst.set(s);
        setOtherValues();

    }

    public String getGst() {

        return gst.get();

    }

    public String getIgst() {

        return igst.get();
    }

    public void setIgst(String s) {
        igst.set(s);

    }

    public String getRtax(){
        return rtax.get();
    }
    public void setRtax(String r){
        rtax.set(r);
    }
    private void setOtherValues() {
        if (!(this.getSerialNo().equals("Total")
                || this.getSerialNo().equals("Tax")
                || this.getSerialNo().equals("Sub Total"))) {
            BigDecimal c = BigDecimal.valueOf(
                    Double.parseDouble(this.getCgst().equals("") ? BigDecimal.ZERO.toString() : this.getCgst()));
            BigDecimal s = BigDecimal.valueOf(
                    Double.parseDouble(this.getSgst().equals("") ? BigDecimal.ZERO.toString() : this.getSgst()));
            BigDecimal i = BigDecimal.valueOf(
                    Double.parseDouble(this.getIgst().equals("") ? BigDecimal.ZERO.toString() : this.getIgst()));

            BigDecimal g = c.add(s);


             
            if (! i.equals(BigDecimal.valueOf(Double.parseDouble(BigDecimal.ZERO.toString())))) {
                g = i;
            }
            gst.set(g.toString());

        } else {
            gst.set("");
        }
    }

    public String getAmountValue() {
        return amountValue.get();
    }

    public void setAmountValue(String aValue) {
        amountValue.set(aValue);

    }

    public String getTotalString() {
        return totalString.get();
    }

    //Making it immutable hack
    public void setTotalString(String sTotal) {
        totalString.set(sTotal);
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getCTaxAmount() {
        return cTaxAmount;
    }

    public BigDecimal getSTaxAmount() {
        return sTaxAmount;
    }

    public BigDecimal getITaxAmount() {
        return iTaxAmount;
    }

    private void calculateAmount() {
        if (!(this.getSerialNo().equals("Total")
                || this.getSerialNo().equals("Tax")
                || this.getSerialNo().equals("Sub Total"))) {
            try {

                BigDecimal q = BigDecimal.valueOf(
                        Double.parseDouble(this.getQuantValue()));
                BigDecimal r = BigDecimal.valueOf(
                        Double.parseDouble(this.getRateValue()));
                BigDecimal a = q.multiply(r);

                //Calculate Tax
                BigDecimal c = BigDecimal.valueOf(
                        Double.parseDouble(this.getCgst()));
                BigDecimal s = BigDecimal.valueOf(
                        Double.parseDouble(this.getSgst()));
                BigDecimal i = BigDecimal.valueOf(
                        Double.parseDouble(this.getIgst()));

                cTaxAmount = a.multiply(c).divide(BigDecimal.valueOf(100.0));
                sTaxAmount = a.multiply(s).divide(BigDecimal.valueOf(100.0));
                iTaxAmount = a.multiply(i).divide(BigDecimal.valueOf(100.0));

                if (i.equals(BigDecimal.valueOf(
                        Double.parseDouble(BigDecimal.ZERO.toString())))) {
                    
                    taxAmount = a.multiply(c).divide(BigDecimal.valueOf(100.0))
                            .add(a.multiply(s).divide(BigDecimal.valueOf(100.0)));
                } else {
                    taxAmount = a.multiply(i).divide(BigDecimal.valueOf(100.0));
                }
                setAmountValue(a.toString());
            } catch (NumberFormatException e) {
                setAmountValue("");

            }
        } else {
            this.setCgst("");
            this.setSgst("");
            this.setIgst("");

        }
    }

    private String incrSerialNo() {
        int s_no = Integer.parseInt(getSerialNo());
        s_no++;
        return Integer.toString(s_no);
    }
}
