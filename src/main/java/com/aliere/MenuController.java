package com.aliere;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        Button b = (Button)event.getSource();

        Main.getAlgorithmController().setTitle("Algorithm A");
        Main.getAlgorithmController().addParameterInput(
            new ParameterInput("Parameter 1")
        );
        Main.setScreen(Main.Screen.ALGORITHM);
        //name(event);
    }

    public static void name(ActionEvent event) {
        Button b = (Button)event.getSource();
        System.out.println(b.getText());
        GridPane g = (GridPane)b.getParent();
        EventHandler<ActionEvent> handy = e -> {System.out.println("click!");};
        ParameterInput p = new ParameterInput("Param 1", handy);
        p.setText("testing");
        g.add(p, 1, 2);
    }
    
}
