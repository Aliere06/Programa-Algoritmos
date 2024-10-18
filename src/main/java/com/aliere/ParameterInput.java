package com.aliere;

import java.net.URL;
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
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ParameterInput<T> extends VBox implements Initializable{

    protected static final Ikon DEFAULT_ICON = FontAwesomeRegular.CIRCLE;

    private Parameter<T> parameter;
    private boolean hasButton;

    @FXML private Label label;
    @FXML private TextField textField;
    @FXML private Button button;
    @FXML private FontIcon buttonIcon;
    
    protected ParameterInput(Parameter<T> parameter) {
        this.parameter = parameter;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Parameter.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.setClassLoader(getClass().getClassLoader()); //Needed if your FXML has 3rd party dependencies (e.g., ikonli)
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLabelText(parameter.getName());
        setHasButton(false);
        if (parameter.getButtonAction() != null) {
            setHasButton(true);
            setButtonIcon(parameter.getButtonIcon() == null ? DEFAULT_ICON : parameter.getButtonIcon());
            setButtonAction(parameter.getButtonAction());
        }
        if (parameter.isGenerated()) {
            textField.setEditable(false);
            textField.setStyle("-fx-control-inner-background: lightgray");
        }
    }

    /*
    protected ParameterInput(Parameter<T> parameter, EventHandler<ActionEvent> button_action, boolean isGenerated) {
        this(parameter);
        if (button_action != null) {
            setHasButton(true);
            setButtonIcon(DEFAULT_ICON);
            setButtonAction(button_action);
        }
        if (isGenerated) {
            textField.setEditable(false);
            textField.setStyle("-fx-control-inner-background: lightgray");
        }
        this.isGenerated = isGenerated;
    }
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    textField.textProperty().addListener(new ChangeListener<String>() {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            try {
                parameter.parseString(newValue);
            } catch (Exception e) {
                parameter.invalidate();
                //e.printStackTrace();
            }
            parameter.validate();
            if (!parameter.isValid()) {
                textField.setStyle("-fx-control-inner-background: mistyrose");
                /* TODO: Properly implement error tooltips
                Tooltip t = new Tooltip("Valor Incorrecto");
                textField.setTooltip(t);
                Point2D p = textField.localToScreen(0, textField.getHeight());
                textField.getTooltip().show(textField.getScene().getWindow(), p.getX(), p.getY());
                 */
            } else {
                textField.setStyle("-fx-control-inner-background: white");
            }
        }
        
    });
    }

    public void refreshValue() {
        String input = getInput();
        setInput("");
        setInput(input);
    }

    public void setLabelText(String text) {
        label.setText(text);
    }
    public void setGenerated(boolean isEditable) {
        textField.setEditable(isEditable);
    }
    public void setInput(String text) {
        String style = textField.getStyle();
        textField.setText(text);
        textField.setStyle(style);
    }
    public void setHasButton(boolean hasButton) {
        if (hasButton) {
            ((HBox)textField.getParent()).getChildren().add(button);
        } else {
            ((HBox)textField.getParent()).getChildren().remove(button);
        }
        this.hasButton = hasButton;
    }
    protected void setButtonAction(EventHandler<ActionEvent> action) {
        button.setOnAction(action);
    }
    protected void setButtonIcon(Ikon buttonIcon) {
        this.buttonIcon.setIconCode(buttonIcon);
    }

    public String getName() {
        return label.getText();
    }
    public String getInput() {
        return textField.getText();
    }
    public T getValue() {
        return parameter.getValue();
    }
    public Boolean hasButton() {
        return hasButton;
    }
    protected TextField getTextField() {
        return textField;
    }
    public Parameter<T> getParameter() {
        return parameter;
    }
}
