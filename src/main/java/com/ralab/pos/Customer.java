/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  com.ralab.pos;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author admin
 */
public class Customer {
     private  final SimpleStringProperty fName = new SimpleStringProperty("");
     private  final SimpleStringProperty lName = new SimpleStringProperty("");
     private  final SimpleStringProperty city = new SimpleStringProperty("");
     private  final SimpleStringProperty state = new SimpleStringProperty("");
     private  final SimpleStringProperty phoneNo = new SimpleStringProperty("");
     private  final SimpleStringProperty gstNo = new SimpleStringProperty("");
     private  final SimpleStringProperty aNo = new SimpleStringProperty("");
     
     public static final String dummyPhone = new String("0000000000");
     
     Customer(){
       setPhoneNo(dummyPhone); 
     }
     Customer(String f,
             String l,
             String c,
             String s,
             String p,
             String g,
             String a){
         
     }
     public String getFName(){
         return fName.get();
     }
     public void setFName(String f){
         fName.set(f);
     }
     public String getLName() {
         return lName.get();
     }   
     public void setLName(String l){
         lName.set(l);
     }
     public String getCity(){
         return city.get();
     }
     public void setCity(String c){
         city.set(c);
     }
     public String getState(){
         return state.get();
     }
     public void setState(String s){
         state.set(s);
     }
     public String getPhoneNo(){
         return phoneNo.get();
     }
     public void setPhoneNo(String p){
         phoneNo.set(p);
     }
     public String getGstNo(){
         return gstNo.get();
     }
     public void setGstNo(String g){
         gstNo.set(g);
     }
     public String getANo(){
         return aNo.get();
     }
     public void setANo(String a){
         aNo.set(a);
     }
}
