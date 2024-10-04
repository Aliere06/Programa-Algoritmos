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
    public abstract RandomNumber generate(int iterations);

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
}
