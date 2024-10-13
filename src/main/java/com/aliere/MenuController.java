package com.aliere;

import java.net.URL;
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
        /*
        Algorithm sample_algorithm = Algorithm.SAMPLE;
        Main.getAlgorithmController().setTitle(sample_algorithm.getName());
        Main.getAlgorithmController().setCurrentAlgorithm(sample_algorithm);

        HashMap<String, Parameter<?>> parameters = sample_algorithm.getParameters();
        HashMap<String, ParameterInput<?>> parameterInputs = new HashMap<>();
        for (Parameter<?> p : parameters.values()) {
            parameterInputs.put(p.getName(), new ParameterInput<>(p));
        }
        parameterInputs.get("Iterations").setHasButton(true);
        parameterInputs.get("Iterations").setButtonAction(e -> Algorithm.SAMPLE.generateList());
        Main.getAlgorithmController().addParameterInput(parameterInputs.values().toArray(new ParameterInput[0]));
        */
        Platform.runLater(() -> {
            Main.getAlgorithmController().loadAlgorithm(Algorithm.SAMPLE);
        });
        Main.setScreen(Main.Screen.ALGORITHM);
    }
}
