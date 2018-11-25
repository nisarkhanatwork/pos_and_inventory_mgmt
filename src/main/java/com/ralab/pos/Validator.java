/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.math.BigDecimal;

/**
 *
 * @author lab149
 */
public class Validator {

    public static boolean notValidAmount(String r) {
        try {
            BigDecimal.valueOf(Double.parseDouble(r));
            return false;
        } catch (Exception e) {
            
            return true;
        }
    }
        public static boolean isValidAmount(String r) {
        try {
            BigDecimal.valueOf(Double.parseDouble(r));
            return true;
        } catch (Exception e) {
            
            return false;
        }
    }
        public static boolean validInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }
}
