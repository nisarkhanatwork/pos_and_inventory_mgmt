/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

/**
 *
 * @author lab149
 */
public class Address {

    int sNo;
    String town;
    String pName;
    String addr1;
    String addr2;
    String phone;
    String tin;
    String cLimit;
    String pdc;
    String bk;
    String tpt;
    String ano;

    public Address(){
        sNo = 9999; town = pName = addr1 = addr2 = phone 
                = tin = cLimit = pdc = bk = tpt = ano = new String();
    }
    public Address(
            int s,
            String t,
            String p,
            String a1,
            String a2,
            String ph,
            String ti,
            String c,
            String pd,
            String b,
            String tp,
            String a
    ) {
        this.setSNo(s);
        this.setTown(t);
        this.setPName(p);
        this.setAddr1(a1);
        this.setAddr2(a2);
        this.setPhone(ph);
        this.setTin(ti);
        this.setCLimit(c);
        this.setPdc(pd);
        this.setBk(b);
        this.setTpt(tp);
        this.setANo(a);
    }

    public int getSNo() {
        return sNo;

    }

    public void setSNo(int s) {
        sNo = s;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String t) {
        town = t;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String p) {
        pName = p;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String a1) {
        addr1 = a1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String a2) {
        addr2 = a2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String p) {
        phone = p;
    }

    public String getTin(){
        return tin;
    }
    public void setTin(String ti){
        tin  = ti;
    }
    public String getCLimit() {
        return cLimit;
    }

    public void setCLimit(String c) {
        cLimit = c;
    }

    public String getPdc() {
        return pdc;
    }

    public void setPdc(String p) {
        pdc = p;
    }

    public String getBk() {
        return bk;
    }

    public void setBk(String b) {
        bk = b;
    }

    public String getTpt() {
        return tpt;
    }

    public void setTpt(String t) {
        tpt = t;
    }
    public String getANo(){
        return ano;
    }
    public void setANo(String a){
        ano = a;
    }
    public boolean isDefault(){
        return sNo == 9999;
    }
}
