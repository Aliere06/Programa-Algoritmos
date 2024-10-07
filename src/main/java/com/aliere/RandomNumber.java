package com.aliere;

import java.util.HashMap;

public class RandomNumber {
    private double value;
    private HashMap<String, String> components;
    
    public double getValue() {
        return value;
    }
    public HashMap<String, String> getComponents() {
        return components;
    }
    public String getComponent(String key) {
        return components.get(key);
    }

    public RandomNumber(double value, HashMap<String, String> components) {
        this.value = value;
        this.components = components;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
