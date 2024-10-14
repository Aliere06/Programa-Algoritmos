package com.aliere;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MenuController implements Initializable{
    HashMap<String, Algorithm> buttonMap;

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
            Program.getAlgorithmController().loadAlgorithm(buttonMap.get(((Button)event.getSource()).getText()));
        });
        Program.setScreen(Program.Screen.ALGORITHM);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonMap = new HashMap<>();
        buttonMap.put("Cuadrados Medios", Algorithm.CUADRADOS_MEDIOS);
        buttonMap.put("Productos Medios", Algorithm.PRODUCTOS_MEDIOS);
        buttonMap.put("Multiplicador Constante", Algorithm.MULTIPLICADOR_CONSTANTE);
        buttonMap.put("Lineal", Algorithm.LINEAR);
        buttonMap.put("Congruencial Multiplicativo", null);
        buttonMap.put("Congruencial Aditivo", Algorithm.CONGRUENTIAL_ADDITIVE);
        buttonMap.put("Congruencial no Lineal", null);
    }
}
