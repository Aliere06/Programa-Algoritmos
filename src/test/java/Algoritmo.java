import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Algoritmo {
    private String nombre;
    private String codigo;
    private ArrayList<Parametro<?>> parametros;
    private ArrayList<NumAleatorio> numeros;
    private String[] columnas;

    public Algoritmo(String nombre, String codigo, String[] columnas, Parametro<?>... parametros) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.parametros = new ArrayList<>();
        this.columnas = columnas;
        
        for (Parametro<?> parametro : parametros) {
            this.parametros.add(parametro);
        }
        this.numeros = new ArrayList<>();
    }

    public double generarNumAleatorio(double seed) {
        Long p = (Long) parametros.get(0).validar();
        Long q = (Long) parametros.get(1).validar();
        Long m = (Long) parametros.get(3).validar(); 

        // Validaciones adicionales
        if (p == null || q == null || m == null) {
            throw new IllegalArgumentException("Los parámetros deben ser válidos.");
        }

        if (!esPrimo(p.intValue()) || !esPrimo(q.intValue())) {
            throw new IllegalArgumentException("p y q deben ser primos.");
        }

        if (p % 4 != 3 || q % 4 != 3) {
            throw new IllegalArgumentException("p y q deben cumplir p % 4 == 3 y q % 4 == 3.");
        }

        double xiSquared = seed * seed;
        double modM = xiSquared % m;

        double normalized = modM / (m - 1);
        Map<String, Object> mapaValores = new HashMap<>();
        mapaValores.put(columnas[1], seed);
        mapaValores.put(columnas[2], xiSquared);
        mapaValores.put(columnas[3], modM);
        mapaValores.put(columnas[4], normalized);

        numeros.add(new NumAleatorio(normalized, mapaValores));
        return normalized;
    }

    public static boolean seeds(double seed, Long m) {
        return seed < m && Math.sqrt(seed) % 1 == 0;
    }

    public static boolean esPrimo(int numero) {
        if (numero < 2) return false;
        if (numero == 2) return true;
        if (numero % 2 == 0) return false;

        int top = (int) Math.sqrt(numero) + 1;
        for (int i = 3; i < top; i += 2) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
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

    // Método para imprimir la tabla (sin cambios)
    public void imprimirTabla(double seed) {
        System.out.println("------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-15s | %-15s | %-15s |\n", columnas[0], columnas[1], columnas[2], columnas[3], columnas[4]);
        System.out.println("------------------------------------------------------");

        double xi = seed;

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
}
