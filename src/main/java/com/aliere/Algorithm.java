package com.aliere;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
//import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.regex.*;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import javafx.application.Platform;

//ALGORITHM CLASS
/**<p> Class that represents the algorithm model.
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
    private int originalSeed;
    private int trueSeed;
    private LinkedHashMap<String, Parameter<?>> parameters; //Algorithm's generation parameters
    private LinkedHashSet<RandomNumber> randomNumbers; //List of generated numbers
    private String[] columns; //Column headers for the GUI's table
    
    //SETS
    public void setName(String name) {
        this.name = name;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setOriginalSeed(int originalSeed) {
        this.originalSeed = originalSeed;
    }
    public void setTrueSeed(int trueSeed) {
        this.trueSeed = trueSeed;
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
    public int getOriginalSeed() {
        return originalSeed;
    }
    public int getTrueSeed() {
        return trueSeed;
    }    
    public LinkedHashMap<String, Parameter<?>> getParameters() {
        return parameters;
    }    
    public LinkedHashSet<RandomNumber> getRandomNumbers() {
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
        this.randomNumbers = new LinkedHashSet<>();
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
    public void generateList() {
        try {
            parametersAreValid(); //Verify parameter's values validity
            randomNumbers.clear(); //Clear the random number list

            Platform.runLater(() -> { //Queue execution of the generation of random numbers
                
                int iterations = (int)getParameters().get("Iteraciones").getValue();
                if (iterations == 0) {
                    int runningSeed = -1;
                    if (this == CUADRADOS_MEDIOS || this == PRODUCTOS_MEDIOS || this == MULTIPLICADOR_CONSTANTE) {
                        while (runningSeed != 0) {
                            //System.out.println("inside of the loop");
                            if (randomNumbers.add(generate()) && randomNumbers.size() < 1000) {
                                runningSeed = (int)getParameters().get("Semilla").getValue();
                                //System.out.println("Running seed: " + runningSeed);
                            } else {
                                //System.out.println("repeat value found");
                                runningSeed = 0;
                            }
                        }
                        //System.out.println("out of the loop");
                        //System.out.println(randomNumbers.size());
                    } else {
                        /*For congruential algorithms, step through the first 2 iterations
                        *to determine the true seed*/
                        originalSeed = (int)getParameters().get("Semilla").getValue();
                        //System.out.println("Original seed: " + originalSeed);
                        randomNumbers.add(generate());
                        
                        trueSeed = (int)getParameters().get("Semilla").getValue();
                        //System.out.println("True seed: " + trueSeed);
                        randomNumbers.add(generate());

                        runningSeed = (int)getParameters().get("Semilla").getValue();
                        //System.out.println("Running seed: " + runningSeed);
                        while (runningSeed != trueSeed && runningSeed != originalSeed) {
                            randomNumbers.add(generate());
                            runningSeed = (int)getParameters().get("Semilla").getValue();
                            //System.out.println("Running seed: " + runningSeed);
                        }
                    }

                } else {
                    for (int i = 0; i < iterations; i++) {
                        if (!randomNumbers.add(generate())) {
                            //System.out.println("valor repetido");
                        }
                        ; //Generate numbers up to the iteration count
                    }
                }

                //System.out.println("building table");
                Program.getAlgorithmController().populateTable(randomNumbers); //Populate GUI table
                //System.out.println("Table built");
                for (ParameterInput<?> pInput : Program.getAlgorithmController().getParameterInputs().values()) {
                    pInput.refreshValue();
                }
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

    // CUADRADOS MEDIOS
    public static final Algorithm CUADRADOS_MEDIOS = new Algorithm(
        "Algoritmo de Cuadrados Medios", "code", new String[]{"Xn", "Xn^2", "Xn+1", "Ri"}, 
        Parameter.seedParameter(), 
        Parameter.iterationsParameter(e -> Algorithm.CUADRADOS_MEDIOS.generateList())) {

        @SuppressWarnings("unchecked")
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();

            //Generation steps
            int x0 = (int)getParameters().get("Semilla").getValue();
            components.put(getColumns()[0], String.valueOf(x0));

            long x0_squared = x0 * x0;
            components.put(getColumns()[1], String.valueOf(x0_squared));

            int xn = (int)((x0_squared / 100) % 10000); //TODO: add support for n>3 digit numbers, not just 4
            components.put(getColumns()[2], String.valueOf(xn));

            double ri = (double)xn / 10000;
            components.put(getColumns()[3], String.valueOf(ri));

            //Update parameter values
            Parameter<Integer> semilla = (Parameter<Integer>) getParameters().get("Semilla");
            semilla.setValue(xn);

            //Build and return RandomNumber
            RandomNumber ran = new RandomNumber(ri, components); // Crear un nuevo número aleatorio
            return ran;
        }
        
    };

    // PRODUCTO MEDIO ALGORITHM INSTANCE
    public static final Algorithm PRODUCTOS_MEDIOS = new Algorithm(
        "Algoritmo de Productos Medios", "code", new String[]{"X1", "X2", "X1 * X2", "Xn+1", "Ri"},
        //PARAMETERS
        Parameter.seedParameter(), 
        Parameter.iterationsParameter(e -> Algorithm.PRODUCTOS_MEDIOS.generateList()), 
        new Parameter<Integer>("Segunda Semilla", 0){

            @Override
            public Integer validate() {
                Integer seed = (Integer)Algorithm.PRODUCTOS_MEDIOS.parameters.get("Semilla").getValue();
                if (getValue().toString().length() == seed.toString().length()) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) {
                setValue(Integer.parseInt(string));
            }
        }
    ) {
        //GENERATION METHOD
        @SuppressWarnings("unchecked")
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();

            //Generation steps
            int x0 = (int)getParameters().get("Semilla").getValue();
            components.put(getColumns()[0], String.valueOf(x0));

            int x1 = (int)getParameters().get("Segunda Semilla").getValue();
            components.put(getColumns()[1], String.valueOf(x1));
            
            long resultado = (long)x0 * x1;  // Multiplicar X0 y X1
            components.put(getColumns()[2], String.valueOf(resultado));

            int xn = (int)((resultado / 100) % 10000); //TODO: add support for n>3 digit numbers, not just 4
            components.put(getColumns()[3], String.valueOf(xn));

            double ri = (double)xn / 10000;
            components.put(getColumns()[4], String.valueOf(ri));

            //Update parameter values
            Parameter<Integer> seed1 = (Parameter<Integer>) getParameters().get("Semilla");
            seed1.setValue(x1);

            Parameter<Integer> seed2 = (Parameter<Integer>) getParameters().get("Segunda Semilla");
            seed2.setValue(xn);

            //Build and return RandomNumber
            RandomNumber ran = new RandomNumber(ri, components); // Crear un nuevo número aleatorio
            return ran;
        }
    };

    // MULTIPLICADOR CONSTANTE ALGORITHM INSTANCE
    public static final Algorithm MULTIPLICADOR_CONSTANTE = new Algorithm(
        "Multiplicador Constante", "code", new String[]{"Xn", "Xn * a", "Xn+1", "Ri"},
        //PARAMETERS
        Parameter.seedParameter(),
        Parameter.iterationsParameter(e -> Algorithm.MULTIPLICADOR_CONSTANTE.generateList()),
        new Parameter<Long>("Multiplicador (a)", 0L){

            @Override
            public Long validate() {
                if (getValue() > 0) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) {
                setValue(Long.parseLong(string));
            }
        }
    ) {
        //GENERATION METHOD
        @SuppressWarnings("unchecked")
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();
            long a = (long)getParameters().get("Multiplicador (a)").getValue();
            

            int x0 = (int)getParameters().get("Semilla").getValue();
            components.put(getColumns()[0], String.valueOf(x0));
            
            long x0a = x0 * a;
            components.put(getColumns()[1], String.valueOf(x0a));

            int xn = (int)((x0a / 100) % 10000); //TODO: add support for n>3 digit numbers, not just 4
            components.put(getColumns()[2], String.valueOf(xn));

            double ri = (double)xn / 10000;
            components.put(getColumns()[3], String.valueOf(ri));


            Parameter<Integer> seed = (Parameter<Integer>) getParameters().get("Semilla");
            seed.setValue(xn);
    

            RandomNumber ran = new RandomNumber(ri, components);
            return ran;
        }
    };

    // LINEAR CONGRUENTIAL ALGORITHM INSTANCE
    public static final Algorithm LINEAL = new Algorithm(
        "Algoritmo Lineal", "code", new String[]{"Xn", "Xn * a + c", "Mod(m)", "Ri"},
        //PARAMETERS
        Parameter.seedParameter(), 
        Parameter.iterationsParameter(e -> Algorithm.LINEAL.generateList()), 
        new Parameter<Integer>("k", 0) {

            @Override
            public Integer validate() {
                if (getValue() >= 0) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Integer.parseInt(string));
            }
            
        }, 
        new Parameter<Integer>("g", 0) {

            @Override
            public Integer validate() {
                if (getValue() >= 0) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Integer.parseInt(string));
            }
  
        }, 
        new Parameter<Integer>("m", 0, 
            e -> {
                AlgorithmController ac = Program.getAlgorithmController();
                int g = (int)ac.getParameterInputs().get("g").getValue();
                ac.getParameterInputs().get("m").setInput(String.valueOf((int)Math.pow(2, g)));
            }, FontAwesomeSolid.SYNC_ALT, true) {

            @Override
            public Integer validate() {
                if (getValue() > 0) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Integer.parseInt(string));
            }
            
        }, 
        new Parameter<Integer>("c", 0, 
            e -> {
                AlgorithmController ac = Program.getAlgorithmController();
                int m = (int)ac.getParameterInputs().get("m").getValue();                
                ac.getParameterInputs().get("c").setInput(String.valueOf(Parameter.largestRelativePrime(m)));
            }, FontAwesomeSolid.SYNC_ALT, true) {

            @Override
            public Integer validate() {
                if (getValue() > 0) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Integer.parseInt(string));
            }
            
        }, 
        new Parameter<Integer>("a", 0, 
            e -> {
                AlgorithmController ac = Program.getAlgorithmController();
                int k = (int)ac.getParameterInputs().get("k").getValue();                
                ac.getParameterInputs().get("a").setInput(String.valueOf(1 + (4 * k)));
            }, FontAwesomeSolid.SYNC_ALT, true) {

                @Override
                public Integer validate() {
                    if (getValue() > 0) {
                        return getValue();
                    } else {
                        return null;
                    }
                }

                @Override
                public void parseString(String string) throws Exception {
                    setValue(Integer.parseInt(string));
                }
                
            }
    ) {
        //GENERATION METHOD
        @SuppressWarnings("unchecked")
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();

            //Parameter values
            int a = (int)getParameters().get("a").getValue();
            int c = (int)getParameters().get("c").getValue();
            int m = (int)getParameters().get("m").getValue();

            //Generation steps
            int x0 = (int)getParameters().get("Semilla").getValue();
            components.put(getColumns()[0], String.valueOf(x0));

            long xac = ((long)x0 * a) + c;
            components.put(getColumns()[1], String.valueOf(xac));

            int modM = (int)xac % m;
            components.put(getColumns()[2], String.valueOf(modM));

            double ri = (double)modM / (m - 1);
            components.put(getColumns()[3], String.valueOf(ri));

            //Update parameter values
            Parameter<Integer> seed = (Parameter<Integer>)getParameters().get("Semilla");
            seed.setValue(modM);

            //Build and return RandomNumber
            RandomNumber ran = new RandomNumber(ri, components);
            return ran;
        }
    };
    
    // CONGRUENTIAL ADDITIVE ALGORITHM INSTANCE
    public static final Algorithm CONGRUENCIAL_ADITIVO = new Algorithm("Congruential Additive Generator", "code", 
    new String[]{"Xi-1", "Xi-n", "Sum", "Mod(m)", "Ri"},
    //PARAMETERS
    Parameter.seedParameter(),
    new Parameter<String>("Additional seeds", ""){

        Pattern invalidationPattern = Pattern.compile("[^-,\\d\\s]+");
        Pattern numPattern = Pattern.compile("\\d+");

        @Override
        public String validate() {
            int seed = (int)Algorithm.CONGRUENCIAL_ADITIVO.parameters.get("Semilla").getValue();
            
            Matcher invalidMatcher = invalidationPattern.matcher(getValue());
            if (invalidMatcher.find()) {
                return null;
            }

            Matcher numMatcher = numPattern.matcher(getValue());
            int d = String.valueOf(seed).length();
            if (numMatcher.results().allMatch(m -> m.group().length() == d)) {
                /*
                ArrayList<String> matches = new ArrayList<>();
                numMatcher.results().forEach(m -> matches.add(m.toString()));
                value = "";
                value.concat(matches.removeFirst());
                for (String s : matches) {
                    value.concat(", " + s);
                }
                 */
                return getValue();
            } else {
                return null;
            }
        }

        @Override
        public void parseString(String string) throws Exception {
            setValue(string);
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
    new Parameter<Integer>("Iterations", 10, e -> Algorithm.CONGRUENCIAL_ADITIVO.generateList(), FontAwesomeSolid.PLAY, false){

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
    };

    // MULTIPLICATIVE CONGRUENTIAL ALGORITHM
    public static final Algorithm BLUM_BLUM_SHUB = new Algorithm(
        "Algoritmo de Blum Blum Sub", "code", new String[]{"Xn", "Xn^2", "Mod(m)", "Ri"}, 
        Parameter.seedParameter(),
        new Parameter<Integer>("p", 0) {

            @Override
            public Integer validate() {
                if (Parameter.isPrime(getValue()) && getValue() % 4 == 3) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Integer.parseInt(string));
            }
            
        }, 
        new Parameter<Integer>("q", 0) {

            @Override
            public Integer validate() {
                if (Parameter.isPrime(getValue()) && getValue() % 4 == 3) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Integer.parseInt(string));
            }
            
        }, 
        new Parameter<Long>("m", 0L, 
            e -> {
                AlgorithmController ac = Program.getAlgorithmController();
                int p = (int)ac.getParameterInputs().get("p").getValue();
                int q = (int)ac.getParameterInputs().get("q").getValue();
                ac.getParameterInputs().get("m").setInput(String.valueOf(p * q));
            }, FontAwesomeSolid.SYNC_ALT, true) {

            @Override
            public Long validate() {
                int p = (int)Algorithm.BLUM_BLUM_SHUB.parameters.get("p").getValue();
                int q = (int)Algorithm.BLUM_BLUM_SHUB.parameters.get("q").getValue();
                if (getValue() == p * q) {
                    return getValue();
                } else {
                    return null;
                }
            }

            @Override
            public void parseString(String string) throws Exception {
                setValue(Long.parseLong(string));
            }

        }, 
        Parameter.iterationsParameter(e -> Algorithm.BLUM_BLUM_SHUB.generateList())
    ) {

        @SuppressWarnings("unchecked")
        @Override
        public RandomNumber generate() {
            LinkedHashMap<String, String> components = new LinkedHashMap<>();

            //Parameter values
            long m = (long)getParameters().get("m").getValue();

            //Generation steps
            int x0 = (int)getParameters().get("Semilla").getValue();
            components.put(getColumns()[0], String.valueOf(x0));

            long x0_squared = (long)x0 * x0;
            components.put(getColumns()[1], String.valueOf(x0_squared));

            int modM = (int)(x0_squared % m);
            components.put(getColumns()[2], String.valueOf(modM));

            double ri = (double)modM / (m - 1);
            components.put(getColumns()[3], String.valueOf(ri));

            //Update parameter values
            Parameter<Integer> seed = (Parameter<Integer>)getParameters().get("Semilla");
            seed.setValue(modM);

            //Build and return RandomNumber
            RandomNumber ran = new RandomNumber(ri, components);
            return ran;
        }
    };
}
