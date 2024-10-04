public abstract class Parametro<T> {
    private String nombre;
    private T valor;
    private boolean esValido;

    public Parametro(String nombre, T valor) {
        this.nombre = nombre;
        this.valor = valor;
        validarInterno();
    }

    /** Este método regresa el valor original de {@code valor}
     * si este pasa los criterios de validación, los criterios
     * se deben implementar en este mismo método.
     * Si no pasa la validación retorna {@code null}
     */
    abstract public T validar();

    private void validarInterno() {
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

    public T getValor() {
        return valor;
    }
}
