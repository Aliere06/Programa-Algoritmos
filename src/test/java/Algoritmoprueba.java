package com.aliere;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.application.Platform;

//ALGORITHM CLASS
/**
 * <p> Class that represents the algorithm model.
 * 
 * <p> All the algorithms present in the program are defined as instances
 * of this class, the attributes of these instances are used by the
 * algorithm controller to build the algorithm GUI at runtime.
 * 
 * <p> Attributes:
 * <ul>
 * <li> {@code name} Coloquial or display name of the algorithm
 * <li> {@code code} WIP - Generation algorithm code
 * <li> {@code parameters} Map of parameter objects used in the generation algorithm
 * <li> {@code randomNumbers} List of randomNumber objects, to be populated by the generation algorithm
 * <li> {@code columns} Array of column headers for the GUI table
 * </ul>
 */
public abstract class Algorithm {

    //ATTRIBUTES
    private String name; //Algorithm display name
    private String code; //Algorithm generation code
    private LinkedHashMap<String, Parameter<?>> parameters; //Algorithm's generation parameters
    private ArrayList<RandomNumber> randomNumbers; //List of generated numbers
    private String[] columns; //Column headers for the GUI's table
    
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
    public ArrayList<RandomNumber> getRandomNumbers() {
        return randomNumbers;
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
        //Build the parameter map from the varargs parameter array
        for (Parameter<?> parametro : parameters) {
            this.parameters.put(parametro.getName(), parametro);
        }
        this.randomNumbers = new ArrayList<>();
    }

    //PARAMETERS ARE VALID
    /**Loops through the parameter map and checks for invalid parameter values.
     * @return {@code true} if all of the parameter's values are valid.
     * @throws InvalidParameterException if any of the parameter's values are invalid.
     */
    public boolean parametersAreValid() throws InvalidParameterException {
        for (Parameter<?> p : parameters.values()) { //Loop through the parameter map
            if (!p.isValid()) { //Check if parameter value is valid
                throw new InvalidParameterException(
                    String.format("El valor del parámetro \"%s\" no es válido.", p.getName())
                );
            }
        }
        return true;
    }

    //GENERATION METHODS
        
    //Generate List
    /**<p> Invokes {@link #parametersAreValid()} to check if all parameter values are valid,
     * if they are it generates and adds random numbers to the {@code randomNumbers} 
     * ArrayList up to the {@code Iterations} parameter's value count. Random numbers are
     * generated by calling this instance's implementation of {@link #generate()}.
     * 
     * <p> Once the random number generation is completed, calls 
     * {@link AlgorithmController#populateTable(ArrayList) populateTable()} in 
     * {@code AlgorithmController} and passes the {@code randomNumers} ArrayList to 
     * populate the GUI table.
     * 
     * <p> If generation fails, calls 
     * {@link AlgorithmController#showNotification(String) showNotification()}
     * and passes the expection message to display it in the GUI.
     */
    public void generateList() { //TODO: Generate the entire cycle if the iteration count is 0
        try {
            parametersAreValid(); //Verify parameter's values validity
            randomNumbers.clear(); //Clear the random number list

            Platform.runLater(() -> { //Queue execution of the generation of random numbers

                int iterations = (int)getParameters().get("Iterations").getValue();
                for (int i = 0; i < iterations; i++) {
                    randomNumbers.add(generate()); //Generate numbers up to the iteration count
                }

                Program.getAlgorithmController().populateTable(randomNumbers); //Populate GUI table
            });

        } catch (Exception e) { //If generation fails send error as notification
            Program.getAlgorithmController().showNotification(e.getMessage());
        }

        //for (RandomNumber r : numbers) { System.out.println(r); }
    }

    //Generate
    /**<p> Abstract generation method, each algorithm instance must implement
     * their own random number generation logic here.
     * 
     * <p> This method is called by {@link #generateList()}.
     * 
     * @return
     * <p> A {@code RandomNumber} object that contains the random number 
     * itself and a map of "components" that correspond to the expected entries
     * for each of the GUI table columns. These usually correspond to different 
     * steps in the generation algorithm.
     */
    public abstract RandomNumber generate();

    //EXPORT NUMBERS
    /**<p> Writes the values of all the {@code randomRumber} objects in the
     * {@code randomNumbers} ArrayList to a file.
     * 
     * <p> This method is called by
     * {@link AlgorithmController#exportBtnPress() exportBtnPress()}, where
     * the output file is provided by a save dialog.
     *  
     * @param numbersFile the file into which the random numbers will be written
     * 
     * @throws IOException if an I/O error occurs
     */
    public void exportNumbers(File numbersFile) throws IOException {
        FileWriter writer = new FileWriter(numbersFile);
        for (RandomNumber num : randomNumbers) { //Loop through randomNumbers
            writer.write(num.getValue() + "\n"); //Write the randomNumber value
        }
        writer.close();
    }

    //ALGORITHM INSTANCES
    /* Final instances of the algorithm class, each one of these (except the sample one) 
     * corresponds to one of the buttons in the program's main menu.
     */

    // SAMPLE ALGORITHM INSTANCE
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
            double seed = (double) Program.getAlgorithmController().getParameterInputs().get("Seed").getValue();
            int iterations = (int) Program.getAlgorithmController().getParameterInputs().get("Iterations").getValue();
            Program.getAlgorithmController().getParameterInputs().get("Generated").setInput(String.valueOf(seed * iterations));
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

    // LINEAR CONGRUENTIAL ALGORITHM INSTANCE
    public static final Algorithm LINEAR_CONGRUENTIAL = new Algorithm("Linear Congruential Generator", "LCG", 
    new String[]{"Iteration", "Xn", "Xn+1"},
    //PARAMETERS
    new Parameter<Long>("Seed", 1L){

        @Override
        public Long validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Long.parseLong(string));
        }
    },
    new Parameter<Long>("Multiplier", 48271L){

        @Override
        public Long validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Long.parseLong(string));
        }
    },
    new Parameter<Long>("Increment", 0L){

        @Override
        public Long validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Long.parseLong(string));
        }
    },
    new Parameter<Long>("Modulus", 2147483647L){

        @Override
        public Long validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Long.parseLong(string));
        }
    },
    new Parameter<Integer>("Iterations", 10, e -> Algorithm.LINEAR_CONGRUENTIAL.generateList(), FontAwesomeSolid.PLAY, false){

        @Override
        public Integer validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Integer.parseInt(string));
        }
    }) {
        //GENERATION METHOD
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();
            long seed = (long) getParameters().get("Seed").getValue();
            long multiplier = (long) getParameters().get("Multiplier").getValue();
            long increment = (long) getParameters().get("Increment").getValue();
            long modulus = (long) getParameters().get("Modulus").getValue();
            components.put(getColumns()[0], String.valueOf(seed));

            for (int i = 0; i < (int) getParameters().get("Iterations").getValue(); i++) {
                long next = (multiplier * seed + increment) % modulus;
                components.put(getColumns()[1], String.valueOf(next));
                seed = next;
            }

            RandomNumber ran = new RandomNumber(seed, components);
            return ran;
        }
    };

    // ADDITIONAL CONGRUENTIAL ADDITIVE ALGORITHM INSTANCE
    public static final Algorithm CONGRUENTIAL_ADDITIVE = new Algorithm("Congruential Additive Generator", "code", 
    new String[]{"Iteration", "Xi", "Xi+1"},
    //PARAMETERS
    new Parameter<Long>("Seed", 1L){

        @Override
        public Long validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Long.parseLong(string));
        }
    },
    new Parameter<Long>("Increment", 1L){

        @Override
        public Long validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Long.parseLong(string));
        }
    },
    new Parameter<Integer>("Iterations", 10, e -> Algorithm.CONGRUENTIAL_ADDITIVE.generateList(), FontAwesomeSolid.PLAY, false){

        @Override
        public Integer validate() {
            return getValue();
        }

        @Override
        public void parseString(String string) {
            setValue(Integer.parseInt(string));
        }
    }) {
        //GENERATION METHOD
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();
            long seed = (long) getParameters().get("Seed").getValue();
            long increment = (long) getParameters().get("Increment").getValue();
            components.put(getColumns()[0], String.valueOf(seed));
            long next = seed + increment; // Basic congruential additive generation

            for (int i = 0; i < (int) getParameters().get("Iterations").getValue(); i++) {
                components.put(getColumns()[1], String.valueOf(next));
                seed = next;
                next = seed + increment; // Update to next
            }

            RandomNumber ran = new RandomNumber(seed, components);
            return ran;
        }
                    // PRODUCTO MEDIO ALGORITHM INSTANCE
        public static final Algorithm PRODUCTO_MEDIO = new Algorithm("Producto Medio", "PM", 
            new String[]{"Iteración", "Xn", "Xn+1", "Resultado", "Ri"},
            //PARAMETERS
            new Parameter<Integer>("Semilla X0", 0){

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
            },
            new Parameter<Integer>("Semilla X1", 0){

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
            },
            new Parameter<Integer>("Máx. Iteraciones", 0, e -> PRODUCTO_MEDIO.generateList()){

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
                LinkedHashMap<String, String> components = new LinkedHashMap<>();

                int x0 = (int)getParameters().get("Semilla X0").getValue();
                int x1 = (int)getParameters().get("Semilla X1").getValue();
                
                long resultado = (long)x0 * x1;  // Multiplicar X0 y X1
                String resultadoStr = Long.toString(resultado);
                String digitosCentrales;

                // Obtener los dígitos centrales
                if (resultadoStr.length() == 5) {
                    resultadoStr = "0" + resultadoStr; // Si tiene 5 dígitos, agregar un cero a la izquierda
                    digitosCentrales = resultadoStr.substring(1, 5);
                } else if (resultadoStr.length() == 6) {
                    digitosCentrales = resultadoStr.substring(1, 5); // Quitar el primero y el último
                } else if (resultadoStr.length() == 7) {
                    resultadoStr = "0" + resultadoStr; // Si tiene 7 dígitos, agregar un cero a la izquierda
                    digitosCentrales = resultadoStr.substring(2, 6); // Tomar los centrales
                } else if (resultadoStr.length() > 4) {
                    digitosCentrales = resultadoStr.substring(2, 6); // Si tiene más de 4, tomar los 4 centrales
                } else {
                    digitosCentrales = resultadoStr; // Usar el número completo si tiene 4 o menos
                }

                String ri = "0." + digitosCentrales; // Ri será "0." seguido de los 4 dígitos centrales

                // Añadir valores a los componentes
                components.put(getColumns()[0], String.valueOf(getParameters().get("Iteración").getValue()));
                components.put(getColumns()[1], String.valueOf(x0));
                components.put(getColumns()[2], String.valueOf(x1));
                components.put(getColumns()[3], resultadoStr);
                components.put(getColumns()[4], digitosCentrales);
                components.put(getColumns()[5], ri);

                RandomNumber ran = new RandomNumber(Double.parseDouble(ri), components); // Crear un nuevo número aleatorio
                return ran;
                }
                // MULTIPLICADOR CONSTANTE ALGORITHM INSTANCE
public static final Algorithm MULTIPLICADOR_CONSTANTE = new Algorithm("Multiplicador Constante", "MC", 
new String[]{"Iteración", "Xn", "Xn+1"},
//PARAMETERS
new Parameter<Long>("Seed", 1L){

    @Override
    public Long validate() {
        return getValue();
    }

    @Override
    public void parseString(String string) {
        setValue(Long.parseLong(string));
    }
},
new Parameter<Long>("Multiplier", 5L){

    @Override
    public Long validate() {
        return getValue();
    }

    @Override
    public void parseString(String string) {
        setValue(Long.parseLong(string));
    }
},
new Parameter<Long>("Modulus", 100L){

    @Override
    public Long validate() {
        return getValue();
    }

    @Override
    public void parseString(String string) {
        setValue(Long.parseLong(string));
    }
},
new Parameter<Integer>("Iterations", 10, e -> Algorithm.MULTIPLICADOR_CONSTANTE.generateList(), FontAwesomeSolid.PLAY, false){

    @Override
    public Integer validate() {
        return getValue();
    }

    @Override
    public void parseString(String string) {
        setValue(Integer.parseInt(string));
    }
}) {
    //GENERATION METHOD
    @Override
    public RandomNumber generate() {
        LinkedHashMap<String, String> components = new LinkedHashMap<>();
        long seed = (long) getParameters().get("Seed").getValue();
        long multiplier = (long) getParameters().get("Multiplier").getValue();
        long modulus = (long) getParameters().get("Modulus").getValue();

        components.put(getColumns()[0], String.valueOf(seed));

        for (int i = 0; i < (int) getParameters().get("Iterations").getValue(); i++) {
            long next = (multiplier * seed) % modulus;
            components.put(getColumns()[1], String.valueOf(next));
            seed = next;
        }

        RandomNumber ran = new RandomNumber(seed, components);
        return ran;
    }
};

        };
    };
}
