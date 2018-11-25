/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author lab149
 */
public class Product {
    private  final SimpleStringProperty compName = new SimpleStringProperty("");
    private  final SimpleStringProperty mfgName = new SimpleStringProperty("");
    private  final SimpleStringProperty units = new SimpleStringProperty("");
    private  final SimpleStringProperty price = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private  final SimpleStringProperty prodCode = new SimpleStringProperty("");
    private  final SimpleStringProperty cgst = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private  final SimpleStringProperty sgst = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private  final SimpleStringProperty igst = new SimpleStringProperty(BigDecimal.ZERO.toString());
    private  final SimpleStringProperty hsnCode = new SimpleStringProperty("");
    private  final SimpleStringProperty lowWarn = new SimpleStringProperty("");
}
