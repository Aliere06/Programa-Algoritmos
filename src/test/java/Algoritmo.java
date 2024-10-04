import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Algoritmo {
    private String nombre;
    private String codigo;
    private ArrayList<Parametro<?>> parametros;
    private ArrayList<NumAleatorio> numeros;
    private String[] columnas = {"Iteración", "Xi", "Xi^2", "mod M", "Xi / M-1"};

    public Algoritmo(String nombre, String codigo, Parametro<?>... parametros) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.parametros = new ArrayList<>();
        for (Parametro<?> parametro : parametros) {
            this.parametros.add(parametro);
        }
        this.numeros = new ArrayList<>();
    }

    public BigInteger generarNumAleatorio(BigInteger seed) {
        BigInteger p = (BigInteger) parametros.get(0).validar();
        BigInteger q = (BigInteger) parametros.get(1).validar();
        BigInteger m = (BigInteger) parametros.get(2).validar();

        if (!esPrimo(p) || !esPrimo(q)) {
            throw new IllegalArgumentException("p y q deben ser primos.");
        }
        if (p.mod(BigInteger.valueOf(4)).intValue() != 3 || q.mod(BigInteger.valueOf(4)).intValue() != 3) {
            throw new IllegalArgumentException("p y q deben cumplir p % 4 == 3 y q % 4 == 3.");
        }

        public static boolean seeds(BigInteger seed, BigInteger m) {
            return seed.compareTo(m) < 0 && Math.sqrt(seed.doubleValue()) % 1 == 0;
        }

        BigInteger xiSquared = seed.pow(2);
        BigInteger modM = xiSquared.mod(m);

        if (modM.compareTo(BigInteger.ZERO) < 0) {
            modM = modM.abs();
        }

        double normalized = modM.doubleValue() / (m.doubleValue() - 1);

        Map<String, Object> mapaValores = new HashMap<>();
        mapaValores.put(columnas[1], seed);
        mapaValores.put(columnas[2], xiSquared);
        mapaValores.put(columnas[3], modM);
        mapaValores.put(columnas[4], normalized);

        numeros.add(new NumAleatorio(modM, mapaValores));

        return modM;
    }

    boolean esPrimo(BigInteger numero) {
        return numero.isProbablePrime(10);
    }

    public void imprimirTabla(BigInteger seed) {
        System.out.println("------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s |\n", columnas[0], columnas[1], columnas[2], columnas[3], columnas[4]);
        System.out.println("------------------------------------------------------");

        BigInteger xi = seed;

        for (int i = 1; i <= 10; i++) {
            xi = generarNumAleatorio(xi);

            NumAleatorio num = numeros.get(i - 1);
            Map<String, Object> valores = num.getValores();

            System.out.printf("| %-10d | %-15s | %-15s | %-15s | %-15.4f |\n",
                    i, valores.get(columnas[1]).toString(), valores.get(columnas[2]).toString(),
                    valores.get(columnas[3]).toString(), (double) valores.get(columnas[4]));
        }

        System.out.println("------------------------------------------------------");
    }

    public File exportarNumeros() {
        File archivo = new File("numeros.txt");
        try (FileWriter writer = new FileWriter(archivo)) {
            for (NumAleatorio num : numeros) {
                writer.write(num.getValor() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archivo;
    }
}
