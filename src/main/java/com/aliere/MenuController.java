package com.aliere;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MenuController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void algorithmBtnPress(ActionEvent event) {
        Algorithm sample_algorithm = Algorithm.SAMPLE;
        Main.getAlgorithmController().setTitle(sample_algorithm.getName());

        ArrayList<Parameter<?>> parameters = sample_algorithm.getParameters();
        ArrayList<ParameterInput<?>> parameterInputs = new ArrayList<>();
        Platform.runLater(() -> {
            for (Parameter<?> p : parameters) {
                parameterInputs.add(new ParameterInput<>(p));
            }
            Main.getAlgorithmController().addParameterInput(parameterInputs.toArray(new ParameterInput[]{}));
            parameterInputs.get(1).setHasButton(true);
            parameterInputs.get(1).setButtonAction(e -> Algorithm.SAMPLE.generateList());
        });

        Main.getAlgorithmController().buildTable(Algorithm.SAMPLE.getColumns());
        
        Main.setScreen(Main.Screen.ALGORITHM);
    }
}
