/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  com.ralab.pos;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class Main {
    private Stage main = new Stage();
    
    Main(){
         try {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewMain.fxml"));
        Scene scene = new Scene(root, 1100, 650);
        
      //  main.getIcons().add(new Image(getClass().getResourceAsStream("/ralab_logo.jpg")));
     //   scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toString());
        main.setTitle(Utils.getCompanyName());
            main.setScene(scene);
          main.setResizable(false);
        }catch(Exception e){
             Alert  error = new Alert(Alert.AlertType.ERROR);
                    error.setContentText("Main " + e.getMessage());
                   error.showAndWait();
        }
    }
    public Stage getStage(){
        return main;
    }
    
    
    
}
