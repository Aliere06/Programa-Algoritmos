import java.math.BigInteger;
import java.util.HashMap;

public class NumAleatorio {
    private BigInteger valor; //double
    //hashmap<String,String> componentes

    public NumAleatorio(BigInteger valor, HashMap componentes) {
        this.valor = valor;
    }

    public BigInteger getValor() {
        return valor;
    }

    public double getValorNormalizado(BigInteger m) {
        return valor.doubleValue() / m.doubleValue();
    }
}
