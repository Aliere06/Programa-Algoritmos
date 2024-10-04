import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Algoritmo {
    private String nombre;
    private String codigo;
    private List<Parametro<?>> parametros; //Tiene que ser ArrayList
    private List<NumAleatorio> numeros;
    //String[] columnas

    public Algoritmo(String nombre, String codigo, Parametro<?>... parametros) {// parametros son un arreglo
        this.nombre = nombre;
        this.codigo = codigo;
        this.parametros = parametros;
        this.numeros = new ArrayList<>();
    }

    public BigInteger generarNumAleatorio(BigInteger seed) { //tiene que regresar un numero aleatorio y es abstracto

        //En base a la semilla, calcular el siguiente num A. regresarlo como NumeroAleatorio junto con el mapa
        //de los componentes que corresponden a cada columna del arreglo, y agregarlo a la lista de num A. del algoritmo
        BigInteger p = (BigInteger) parametros.get(0).validar();
        BigInteger q = (BigInteger) parametros.get(1).validar();
        BigInteger m = (BigInteger) parametros.get(3).validar();

        if (!esPrimo(p) || !esPrimo(q)) {
            throw new IllegalArgumentException("p y q deben ser primos.");
        }
        if (p.mod(BigInteger.valueOf(4)).intValue() != 3 || q.mod(BigInteger.valueOf(4)).intValue() != 3) {
            throw new IllegalArgumentException("p y q deben cumplir p % 4 == 3 y q % 4 == 3.");
        }

        BigInteger x = seed.multiply(seed).mod(m);
        if (x.compareTo(BigInteger.ZERO) < 0) {
            x = x.abs();
        }

        numeros.add(new NumAleatorio(x));
        return x;
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

    public void imprimirTabla(BigInteger initialSeed) {
        BigInteger p = (BigInteger) parametros.get(0).getValor();
        BigInteger q = (BigInteger) parametros.get(1).getValor();
        BigInteger m = (BigInteger) parametros.get(3).getValor();

        System.out.println("\nTabla de Resultados");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-20s %-20s %-20s %-20s %-20s%n",
                "Iteracion", "p", "q", "m", "Semilla", "Num Aleatorio", "Valor Normalizado", "Observaciones");

        BigInteger currentSeed = initialSeed;
        for (int i = 0; i < numeros.size(); i++) {
            NumAleatorio num = numeros.get(i);
            double valorNormalizado = num.getValorNormalizado(m);
            System.out.printf("%-10d %-10d %-10d %-20d %-20d %-20d %-20f %-20s%n",
                    (i + 1), p, q, m, currentSeed, num.getValor(), valorNormalizado, "");
            currentSeed = num.getValor();
        }
    }

    public static boolean esPrimo(BigInteger n) {
        return n.isProbablePrime(10);
    }
}
