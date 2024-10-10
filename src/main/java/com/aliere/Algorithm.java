package com.aliere;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;

public abstract class Algorithm {
    private String name;
    private String code;
    private ArrayList<Parameter<?>> parameters;
    private ArrayList<RandomNumber> numbers;
    private String[] columns;
    
    //SETS
    public void setName(String name) {
        this.name = name;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    //GETS
    public String getName() {
        return name;
    }    
    public String getCode() {
        return code;
    }    
    public ArrayList<Parameter<?>> getParameters() {
        return parameters;
    }    
    public ArrayList<RandomNumber> getNumbers() {
        return numbers;
    }    
    public String[] getColumns() {
        return columns;
    }    
    
    //CONSTRUCTOR
    public Algorithm(String nombre, String codigo, String[] columns, Parameter<?>... parametros) {
        this.name = nombre;
        this.code = codigo;
        this.columns = columns;
        this.parameters = new ArrayList<>();
        for (Parameter<?> parametro : parametros) {
            this.parameters.add(parametro);
        }
        this.numbers = new ArrayList<>();
    }

    //GENERATION
    public boolean parametersAreValid() {
        for (Parameter<?> p : parameters) {
            if (!p.isValid()) {
                return false;
            }
        }
        return true;
    }

    public void generateList() {
        if (!parametersAreValid()) {
            return;
        }
        numbers.clear();
        Platform.runLater(() -> {
            int iterations = (int)getParameters().getLast().getValue();
            for (int i = 0; i < iterations; i++) {
                numbers.add(generate());
            }
            Main.getAlgorithmController().populateTable(numbers);
        });
        //for (RandomNumber r : numbers) { System.out.println(r); }
    }
    public abstract RandomNumber generate();

    public void exportarNumeros(File numbersFile) throws IOException {
        FileWriter writer = new FileWriter(numbersFile);
        for (RandomNumber num : numbers) {
            writer.write(num.getValue() + "\n");
        }
        writer.close();
    }

    /* SAMPLE ALGORITHM
     * - Name
     * - Generation code (optional)
     * - Table column headers
     * - PARAMETERS
     */

    public static final Algorithm SAMPLE = new Algorithm("Sample Algorithm", "code", new String[]{"fixed add", "Xi"},
    //PARAMETERS
    new Parameter<Double>("Double Parameter", 0.0){

        @Override
        public Double validate() {
            if (getValue() > 0) {
                return getValue();
            } else {
                return null;
            }
        }

        @Override
        public void parseString(String string) {
            setValue(Double.parseDouble(string));
        }
    },
    new Parameter<Integer>("Iterations", 0){

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
    }) {
        //GENERATION METHOD
        @Override
        public RandomNumber generate() {
            HashMap<String, String> components = new HashMap<>();

            double param1 = (double)getParameters().get(0).getValue();
            components.put(getColumns()[0], String.valueOf(param1));

            double r = Math.random() + param1;
            components.put(getColumns()[1], String.valueOf(r));

            RandomNumber ran = new RandomNumber(r, components);
            System.out.println(components);
            return ran;
        }
    };
}
