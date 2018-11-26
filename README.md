# pos_and_inventory_mgmt
point of sale and inventory management software demo for indian market
This project gives an idea of how a point of sale and inventory management software can be implemented.
The following are the points to note:
1) This is a netbeans project
2) maven repositories are used
3) itextpdf is used to generate pdf bills and reports
4) you have to create databases with the "three" sql files (jaasdb.sql, tractor.sql, and tractor_bkp.sql)
5) for the database passwords please refer to Utils.java

Many changes can be done to improve the code and depending on your requirements, database design can be changed and queries can be tuned.

Note 1 : Utils.java has encryption options, please read the code in Utils.java commented code where AES function calls are there. Use the AES.java and JavaApplication.java files for encryption or you can use your own.

Note 2: Some part of the code is either copied from the web (which is freely distributed) or from stackoverflow.com snippets. I apologize for not giving proper credit. [Please read the license file too] 
