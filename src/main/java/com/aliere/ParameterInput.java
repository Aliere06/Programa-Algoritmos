package com.aliere;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ParameterInput<T> extends VBox implements Initializable{

    public static final Ikon DEFAULT_ICON = FontAwesomeRegular.CIRCLE;

    private Parameter<T> parameter;
    private boolean hasButton;

    @FXML private Label label;
    @FXML private TextField textField;
    @FXML private Button button;
    @FXML private FontIcon buttonIcon;
    
    private ParameterInput() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Parameter.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.setClassLoader(getClass().getClassLoader()); //Needed if your FXML has 3rd party dependencies (e.g., ikonli)
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHasButton(false);
    }

    public ParameterInput(Parameter<T> parameter) {
        this();
        setText(parameter.getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    textField.textProperty().addListener(new ChangeListener<String>() {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            try {
                parameter.parseString(newValue);
            } catch (Exception e) {
                // TODO: handle exception
            }
            parameter.validate();
        }
        
    });
    }

    public ParameterInput(String text) {
        this();
        setText(text);
    }
    public ParameterInput(String text, EventHandler<ActionEvent> button_action) {
        this();
        setHasButton(true);
        setButtonIcon(DEFAULT_ICON);
        setButtonAction(button_action);
    }

    public void setText(String text) {
        label.setText(text);
    }
    public void setHasButton(boolean hasButton) {
        if (hasButton) {
            ((HBox)textField.getParent()).getChildren().add(button);
        } else {
            ((HBox)textField.getParent()).getChildren().remove(button);
        }
        this.hasButton = hasButton;
    }
    public void setButtonAction(EventHandler<ActionEvent> action) {
        button.setOnAction(action);
    }
    public void setButtonIcon(Ikon buttonIcon) {
        this.buttonIcon.setIconCode(buttonIcon);
    }

    public String getInput() {
        return textField.getText();
    }
    public Boolean getHasButton() {
        return hasButton;
    }
}
