import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un número primo p: ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.print("Ingrese un número primo q: ");
        BigInteger q = new BigInteger(scanner.nextLine());

        System.out.print("Ingrese la semilla (x0): ");
        BigInteger seed = new BigInteger(scanner.nextLine());

        BigInteger m = p.multiply(q);

        List<Parametro<?>> parametros = new ArrayList<>();

        parametros.add(new Parametro<BigInteger>("p", p){
            @Override
            public BigInteger validar() {
                Algoritmo.esPrimo(p);
            }// usar este como ejemplo
        });
        parametros.add(new Parametro<BigInteger>("q", q){
            @Override
            public BigInteger validar() {
                Algoritmo.esPrimo(q);
            }
        });
        parametros.add(new Parametro<>("x0", null){
            @Override
            public BigInteger validar(){
                Algoritmo.seeds(seed);
            }
        });//("x0", null, seed.compareTo(m) < 0 && Math.sqrt(seed.doubleValue()) % 1 == 0, () -> {
            //System.out.println("Error: La semilla no es válida.");
            //return null;
        parametros.add(new Parametro<>("m", m, true, () -> {
            System.out.println("Error: m no es válido.");
            return null;
        }));

        Algoritmo bbs = new Algoritmo("Blum Blum Shub", "BBS", parametros);

        for (int i = 0; i < 10; i++) {
            BigInteger numAleatorio = bbs.generarNumAleatorio(seed);
            seed = numAleatorio; // Actualizar la semilla para la siguiente iteración
        }

        bbs.imprimirTabla(parametros.get(2).getValor());
        bbs.exportarNumeros();

        scanner.close();
    }
}
