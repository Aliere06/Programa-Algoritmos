package com.aliere;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;

public abstract class Algorithm {
    private String name;
    private String code;
    private LinkedHashMap<String, Parameter<?>> parameters;
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
    public LinkedHashMap<String, Parameter<?>> getParameters() {
        return parameters;
    }    
    public ArrayList<RandomNumber> getNumbers() {
        return numbers;
    }    
    public String[] getColumns() {
        return columns;
    }    
    
    //CONSTRUCTOR
    public Algorithm(String name, String code, String[] columns, Parameter<?>... parameters) {
        this.name = name;
        this.code = code;
        this.columns = columns;
        this.parameters = new LinkedHashMap<>();
        for (Parameter<?> parametro : parameters) {
            this.parameters.put(parametro.getName(), parametro);
        }
        this.numbers = new ArrayList<>();
    }

    //GENERATION
    public boolean parametersAreValid() {
        for (Parameter<?> p : parameters.values()) {
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
            int iterations = (int)getParameters().get("Iterations").getValue();
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
    new Parameter<Double>("Seed", 0.0){

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
    new Parameter<Integer>("Iterations", 0, e -> Algorithm.SAMPLE.generateList(), FontAwesomeSolid.PLAY, false){

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
    }, 
    new Parameter<String>("Generated", "", 
        e -> {
            double seed = (double) Main.getAlgorithmController().getParameterInputs().get("Seed").getValue();
            int iterations = (int) Main.getAlgorithmController().getParameterInputs().get("Iterations").getValue();
            Main.getAlgorithmController().getParameterInputs().get("Generated").setInput(String.valueOf(seed * iterations));
        }, FontAwesomeSolid.SYNC_ALT, true){

        @Override
        public String validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) throws Exception {
            setValue(getValue());
        }
    }) {
        //GENERATION METHOD
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();

            double param1 = (double)getParameters().get("Seed").getValue();
            components.put(getColumns()[0], String.valueOf(param1));

            double r = Math.random() + param1;
            components.put(getColumns()[1], String.valueOf(r));

            RandomNumber ran = new RandomNumber(r, components);
            System.out.println(components);
            return ran;
        }
    };
}
