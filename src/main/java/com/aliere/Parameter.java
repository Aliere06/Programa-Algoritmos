package com.aliere;

import java.util.LinkedHashMap;

import org.kordamp.ikonli.Ikon;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class Parameter<T> {
    private String name;
    private T value;
    private Class<?> type;
    private boolean isValid;
    private EventHandler<ActionEvent> buttonAction;
    private Ikon buttonIcon;
    private boolean isGenerated;
    
    //SETS
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(T valor) {
        this.value = valor;
        validateInternal();
    }
    protected void setButtonAction(EventHandler<ActionEvent> accion) {
        this.buttonAction = accion;
    }
    protected void setButtonIcon(Ikon buttonIcon) {
        this.buttonIcon = buttonIcon;
    }
    public void setGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }

    //GETS
    public String getName() {
        return name;
    }
    public T getValue() {
        return value;
    }
    public Class<?> getType() {
        return type;
    }
    protected EventHandler<ActionEvent> getButtonAction() {
        return buttonAction;
    }
    protected Ikon getButtonIcon() {
        return buttonIcon;
    }
    public boolean isGenerated() {
        return isGenerated;
    }
    
    //CONSTRUCTOR
    public Parameter(String name, T value) {
        this.name = name;
        this.value = value;
        type = value.getClass();
        validateInternal();
        this.buttonAction = null;
        this.isGenerated = false;
    }

    protected Parameter(String name, T value, EventHandler<ActionEvent> action, Ikon buttonIcon, boolean isGenerated) {
        this(name, value);
        if (action != null) {
            this.buttonAction = action;
        }
        this.buttonIcon = buttonIcon;
        this.isGenerated = isGenerated;
    }
    
    //VALIDATION
    /**Returns original value of {@code value} if it's valid
     * else return {@code null}
     */
    abstract public T validate();
    
    private void validateInternal() {
        if (value == validate() && value != null) {
            isValid = true;
        } else {
            isValid = false;
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public void invalidate() {
        isValid = false;
    }

    /**Attempts to parse a string input into type T,
     * must set the parameter's {@code value} as the parsed result
     */
    abstract public void parseString(String string) throws Exception;

    public void bind(LinkedHashMap<String, ParameterInput<?>> parameterInputs) {
    }
}
