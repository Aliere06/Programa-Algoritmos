package com.aliere;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AlgorithmController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    @FXML
    public void backBtnPress() {
        ScreenSwitcher.switchScreen(Main.Screen.MENU);
    }
}
