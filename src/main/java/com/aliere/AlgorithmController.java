package com.aliere;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AlgorithmController{

    @FXML private Label titleLabel;
    @FXML private VBox parameterVBox;

    public  void setTitle(String text) {
        titleLabel.setText(text);
    }
    
    @FXML
    public void backBtnPress() {
        Main.setScreen(Main.Screen.MENU);
    }
}
