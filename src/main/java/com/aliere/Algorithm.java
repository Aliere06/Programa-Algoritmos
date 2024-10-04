package com.aliere;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    public Algorithm(String nombre, String codigo, Parameter<?>... parametros) {
        this.name = nombre;
        this.code = codigo;
        this.parameters = new ArrayList<>();
        for (Parameter<?> parametro : parametros) {
            this.parameters.add(parametro);
        }
        this.numbers = new ArrayList<>();
    }

    //GENERATE METHOD
    public abstract RandomNumber generate();

    public File exportarNumeros() {
        File archivo = new File("numeros.txt");
        try (FileWriter writer = new FileWriter(archivo)) {
            for (RandomNumber num : numbers) {
                writer.write(num.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archivo;
    }

    public static final Algorithm SAMPLE = new Algorithm("Sample Algorithm", "code", 
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
    },
    new Parameter<Integer>("Int Parameter", 0){

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

        @Override
        public RandomNumber generate() {
            int iterations = (int)getParameters().get(0).getValue();
            int param1 = (int)getParameters().get(1).getValue();

            for (int i = 0; i <= iterations; i++) {
                RandomNumber r = new RandomNumber(Math.random() + param1, null);
                System.out.println(r);
            }
            return null;
        }
        
    };
}
