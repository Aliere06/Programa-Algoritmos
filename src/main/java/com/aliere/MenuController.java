package com.aliere;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MenuController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void algorithmBtnPress() {
        ScreenSwitcher.switchScreen(Main.Screen.ALGORITHM);
    }
    
}
