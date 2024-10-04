package com.aliere;

import java.util.HashMap;

public class RandomNumber {
    private double value;
    HashMap<String,String> components;
    
    public double getValue() {
        return value;
    }

    public RandomNumber(double value, HashMap<String, String> components) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
