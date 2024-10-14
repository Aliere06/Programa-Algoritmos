package com.aliere;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class Parameter<T> {
    private String name;
    private T value; //TODO: Change value to an observable property to allow for bindings
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

    //M. IS PRIME
    public static boolean isPrime(long num) {
        if ((num <= 1) || (num > 2 && num%2 == 0)) {
            return false;
        }
        long top = (long)Math.sqrt(num) + 1;
        for(int i = 3; i < top; i+=2){
            if(num % i == 0){
                return false;
            }
        }
        return true;
    }

    //M. LARGEST RELATIVE PRIME
    public static long largestRelativePrime(long num) {
        for (long i = num - 1; i > 0; i--) {
            if (greatestCommonDivisor(i, num) == 1) {
                return i;
            }
        }
        return 2;
    }

    //M. GREATEST COMMON DIVISOR
    public static long greatestCommonDivisor(long a, long b) {
        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException("Inputs must be positive");
        }
        while (a != b) {
            if (Math.abs(a - b) == 1) {
                return 1;
            } else if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }
        return a;
    }

    /**Attempts to parse a string input into type T,
     * must set the parameter's {@code value} as the parsed result
     */
    abstract public void parseString(String string) throws Exception;

    public static Parameter<Integer> seedParameter() {
        return new Parameter<Integer>("Semilla", 0) {

            @Override
            public Integer validate() {
                if (getValue() > 0) {
                    return getValue();
                } else {
                    return null;
                }
            }
    
            @Override
            public void parseString(String string) {
                setValue(Integer.parseInt(string));
            }
        };
    }

    protected static Parameter<Integer> iterationsParameter(EventHandler<ActionEvent> buttonAction) {
        return new Parameter<Integer>("Iteraciones", 0, buttonAction, FontAwesomeSolid.PLAY, false){

            @Override
            public Integer validate() {
                if (getValue() >= 0) {
                    return getValue();
                } else {
                    return null;
                }
            }
    
            @Override
            public void parseString(String string) {
                setValue(Integer.parseInt(string));
            }
        };
    }
}
