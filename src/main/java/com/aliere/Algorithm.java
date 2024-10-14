package com.aliere;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

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
    public boolean parametersAreValid() throws InvalidParameterException{
        for (Parameter<?> p : parameters.values()) { //Loop through the parameter map
            if (!p.isValid()) { //Check if parameter value is valid
                throw new InvalidParameterException(
                    String.format("Value for parameter \"%s\" is invalid", p.getName())
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

                Main.getAlgorithmController().populateTable(randomNumbers); //Populate GUI table
            });

        } catch (Exception e) { //If generation fails send error as notification
            Main.getAlgorithmController().showNotification(e.getMessage());
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
