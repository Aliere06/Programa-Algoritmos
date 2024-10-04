package com.aliere;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class JFXParameter extends VBox {

    public final Ikon DEFAULT_ICON = FontAwesomeRegular.CIRCLE;

    private boolean hasButton;

    @FXML Label label;
    @FXML TextField textField;
    @FXML Button button;
    @FXML FontIcon buttonIcon;
    
    public JFXParameter() {
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

    public JFXParameter(String text) {
        this();
        setText(text);
    }
    public JFXParameter(String text, EventHandler<ActionEvent> button_action) {
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
