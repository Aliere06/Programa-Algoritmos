package com.aliere;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MenuController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void algorithmBtnPress(ActionEvent event) {
        Main.setScreen(Main.Screen.ALGORITHM);
    }

    public static void name(ActionEvent event) {
        Button b = (Button)event.getSource();
        System.out.println(b.getText());
        GridPane g = (GridPane)b.getParent();
        JFXParameter p = new JFXParameter();
        p.setText("testing");
        //g.add(p, 1, 2);
    }
    
}
