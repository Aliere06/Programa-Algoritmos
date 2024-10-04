import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un número primo p: ");
        Long p = Long.parseLong(scanner.nextLine());

        System.out.print("Ingrese un número primo q: ");
        Long q = Long.parseLong(scanner.nextLine());

        System.out.print("Ingrese la semilla (x0): ");
        final double[] seed = {Double.parseDouble(scanner.nextLine())};

        Long m = p * q;

        List<Parametro<?>> parametros = new ArrayList<>();

        parametros.add(new Parametro<Long>("p", p) {
            @Override
            public Long validar() {
                if (!Algoritmo.esPrimo(p.intValue())) {
                    System.out.println("Error: p no es primo.");
                    return null;
                }
                return p;
            }
        });

        parametros.add(new Parametro<Long>("q", q) {
            @Override
            public Long validar() {
                if (!Algoritmo.esPrimo(q.intValue())) {
                    System.out.println("Error: q no es primo.");
                    return null;
                }
                return q;
            }
        });

        parametros.add(new Parametro<Double>("x0", seed[0]) {
            @Override
            public Double validar() {
                if (!Algoritmo.seeds(seed[0], m)) {
                    System.out.println("Error: La semilla no es válida.");
                    return null;
                }
                return seed[0];
            }
        });

        parametros.add(new Parametro<Long>("m", m) {
            @Override
            public Long validar() {
                if (m % p == 0 || m % q == 0) {
                    System.out.println("Error: m no es válido.");
                    return null;
                }
                return m;
            }
        });

        String[] columnas = {"Iteración", "Xi", "Xi^2", "mod M", "Xi / M-1"};
        Algoritmo bbs = new Algoritmo("Blum Blum Shub", "BBS", columnas, parametros.toArray(new Parametro[0]));

        for (int i = 0; i < 10; i++) {
            double numAleatorio = bbs.generarNumAleatorio(seed[0]);
            seed[0] = numAleatorio; // Actualizar la semilla para la siguiente iteración
        }

        bbs.imprimirTabla(seed[0]);
        bbs.exportarNumeros();

        scanner.close();
    }
}
