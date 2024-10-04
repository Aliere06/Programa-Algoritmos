import java.util.HashMap;
import java.util.Map;

public class NumAleatorio {
    private double valor; // Cambiado a double
    private Map<String, Object> componentes; // Cambiado a Map

    public NumAleatorio(double valor, Map<String, Object> componentes) {
        this.valor = valor;
        this.componentes = componentes;
    }

    public double getValor() {
        return valor;
    }

    public double getValorNormalizado(Long m) {
        return valor / m;
    }

    public Map<String, Object> getValores() {
        return componentes;
    }
}
