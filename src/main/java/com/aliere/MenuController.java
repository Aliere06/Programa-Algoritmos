package com.aliere;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        Algorithm sample_algorithm = Algorithm.SAMPLE;
        Main.getAlgorithmController().setTitle(sample_algorithm.getName());

        ArrayList<Parameter<?>> parameters = sample_algorithm.getParameters();
        ArrayList<ParameterInput> parameterInputs = new ArrayList<>();
        for (Parameter<?> p : parameters) {
            parameterInputs.add(new ParameterInput(p.getName()));
        }
        ParameterInput[] a = {};
        Main.getAlgorithmController().addParameterInput(parameterInputs.toArray(a));

        parameterInputs.get(0).setHasButton(true);
        parameterInputs.get(0).setButtonAction(e -> Algorithm.SAMPLE.generate());
        
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
