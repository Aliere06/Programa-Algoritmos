package com.aliere;

import java.util.ArrayList;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class AlgorithmController{

    @FXML private Label titleLabel;
    @FXML private VBox parameterVBox;
    @FXML private TableView<String> tableView;

    public void setTitle(String text) {
        titleLabel.setText(text);
    }
    public void addParameterInput(ParameterInput<?>... parameterInputs) {
        parameterVBox.getChildren().addAll(parameterInputs);
    }
    
    @FXML
    public void backBtnPress() {
        Main.setScreen(Main.Screen.MENU);
    }
}
