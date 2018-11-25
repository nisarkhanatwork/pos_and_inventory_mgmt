/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralab.pos;

import java.util.Scanner;

/**
 *
 * @author https://way2java.com/arrays/converting-numbers-to-words/
 */
public class YourNumberMyWord {

    public String pw(int n, String ch) {
        String out = new String();
        String one[] = {" ", " one", " two", " three", " four", " five", " six", " seven", " eight", " Nine", " ten", " eleven", " twelve", " thirteen", " fourteen", "fifteen", " sixteen", " seventeen", " eighteen", " nineteen"};

        String ten[] = {" ", " ", " twenty", " thirty", " forty", " fifty", " sixty", "seventy", " eighty", " ninety"};

        if (n > 19) {
            out = out.concat(ten[n / 10] + " " + one[n % 10]);
        } else {
            out = out.concat(one[n]);
        }
        if (n > 0) {
            out = out.concat(ch);
        }
        return out;
    }

    public String convert(int n) {
        String temp = new String();
        if (n <= 0) {
            System.out.println("Enter numbers greater than 0");
        } else {
            YourNumberMyWord a = new YourNumberMyWord();
            temp = temp.concat(a.pw((n / 1000000000), " Hundred"));
            temp = temp.concat(a.pw((n / 10000000) % 100, " crore"));
            temp = temp.concat(a.pw(((n / 100000) % 100), " lakh"));
            temp = temp.concat(a.pw(((n / 1000) % 100), " thousand"));
            temp = temp.concat(a.pw(((n / 100) % 10), " hundred"));
            temp = temp.concat(a.pw((n % 100), " "));

        }
        return temp;
    }

}
