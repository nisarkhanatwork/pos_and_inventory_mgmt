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
        //copy constructor
        public Transport(Transport t){
            this.bkTo = t.bkTo;
            this.cases = t.cases;
            this.lrDate = t.lrDate;
            this.lrNo = t.lrNo;
            this.pvtMark = t.pvtMark;
            this.trName = t.trName;
            this.weight  = t.weight;
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