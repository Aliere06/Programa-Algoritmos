package com.aliere;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;

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
        for (Parameter<?> p : parameters) {
            parameterInputs.add(new ParameterInput<>(p));
        }
        Main.getAlgorithmController().addParameterInput(new ParameterInput[]{});

        parameterInputs.get(1).setHasButton(true);
        parameterInputs.get(1).setButtonAction(e -> Algorithm.SAMPLE.generateList());
        
        Main.setScreen(Main.Screen.ALGORITHM);
    }
}
