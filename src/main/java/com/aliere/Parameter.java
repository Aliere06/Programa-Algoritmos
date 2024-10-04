package com.aliere;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class Parameter<T> {
    private String name;
    private T value;
    private Class<?> type;
    private boolean isValid;
    private EventHandler<ActionEvent> action;
    
    //SETS
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(T valor) {
        this.value = valor;
        validateInternal();
    }
    public void setAction(EventHandler<ActionEvent> accion) {
        this.action = accion;
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
    public EventHandler<ActionEvent> getAction() {
        return action;
    }
    
    //CONSTRUCTOR
    public Parameter(String nombre, T valor) {
        this.name = nombre;
        this.value = valor;
        type = valor.getClass();
        validateInternal();
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

    /**Attempts to parse a string input into type T,
     * must set the parameter's {@code value} as the parsed result
     */
    abstract public void parseString(String string);
}
