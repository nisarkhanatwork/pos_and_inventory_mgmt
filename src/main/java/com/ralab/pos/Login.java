/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  com.ralab.pos;


import java.util.Iterator;
import java.util.Properties;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author admin
 */
public class Login {
    private Stage login;
     boolean  window_closed = false;
    Login(){
        login = new Stage();
        login.setTitle(Utils.getCompanyName() + " Software Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text( Utils.getCompanyName() + " Software");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        Scene scene = new Scene(grid, 300, 275);
        login.setScene(scene);
       // login.showAndWait(); 
        btn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
//                if(pwBox.getText().equals("Hello")){
//                    login.close();
//                }
//                actiontarget.setText("Sign in button pressed");
boolean loginSuccess = false;
        Subject subject = null;

        try {
            System.setProperty("java.security.auth.login.config",
                    getClass().getResource("/misc/jaas.config").toString());
            PassiveCallbackHandler cbh = new PassiveCallbackHandler(
                    userTextField.getText(), pwBox.getText());

            LoginContext lc = new LoginContext("Example", cbh);

            try {
                lc.login();
                loginSuccess = true;

                subject = lc.getSubject();

                Iterator it = subject.getPrincipals().iterator();
                while (it.hasNext()) 
                    System.out.println("Authenticated: " + it.next().toString());

                it = subject.getPublicCredentials(Properties.class).iterator();
                while (it.hasNext()) 
                    ((Properties)it.next()).list(System.out);

                //lc.logout();
                if(loginSuccess){
                    login.close();
                } else {
                    actiontarget.setText("Sign in button pressed");
                }
            } catch (LoginException lex) {
                System.out.println(lex.getClass().getName() + ": " + lex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
        }
            }
        });
        login.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent e) {
                
                    
                    window_closed = true;
                
            }
            
        });
        
    }
    public Stage getStage(){
        return login;
    }
    public boolean isWindowClosed(){
        return window_closed;
    }
}
