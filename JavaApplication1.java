/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author nisar
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args)
{
    final String secretKey = "ralabbegins";
     
    String origCompanyName = "INDENCIES";
    String origAddrLine1 = "#4, 18et";
    String origAddrLine2 = "GUNTUR - 503";
    String origPhoneNo = "Phone:  4, 352";
    String origGSTNo = "3J";
    String origKey = origCompanyName;
    
    System.out.println("key = " + AES.encrypt(origKey, secretKey));
    System.out.println("private static String companyName = \"" 
    + AES.encrypt(origCompanyName, secretKey) + "\";");
    System.out.println("private static String addressLine1 = \"" 
    + AES.encrypt(origAddrLine1, secretKey) + "\";");
    System.out.println("private static String addressLine2 = \"" 
    + AES.encrypt(origAddrLine2, secretKey) + "\";");
    System.out.println("private static String phoneNo = \"" 
    + AES.encrypt(origPhoneNo, secretKey) + "\";");
    System.out.println("private static String gstNo = \"" 
    + AES.encrypt(origGSTNo, secretKey) + "\";");
    
    
    YourNumberMyWord w = new YourNumberMyWord();
    System.out.println(w.convert(1123));
    
}

}
