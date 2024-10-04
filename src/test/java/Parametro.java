import java.math.BigInteger;
import java.util.function.Supplier;

public abstract class Parametro<T> {
    private String nombre;
    private T valor;
    private boolean esValido;
    private Runnable accion;

    public Parametro(String nombre, T valor) {
        this.nombre = nombre;
        this.valor = valor;
        validarInterno();
    }

    /**Este método regresa el valor original de {@code valor}
     * sí este pasa los criterios de validación, los criterios
     * se deben implementar en este mismo método.
     * Si no pasa la validación retorna {@code null}
     */
    abstract public T validar();

    private void validarInterno () {
        if (valor == validar() && valor != null) {
            esValido = true;
        } else {
            esValido = false;
        }
    }

    public void setValor(T valor) {
        this.valor = valor;
        validarInterno();
    }

    public void setAccion(Runnable accion) {
        this.accion = accion;
    }
    
    public BigInteger getValor() {
        return (BigInteger) valor;
    }
}
