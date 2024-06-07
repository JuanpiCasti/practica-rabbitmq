package entities;

public class InventarioProducto {
    private String nombre;
    private Integer cantidad;


    public InventarioProducto(String nombre) {
        this.nombre = nombre;
        this.cantidad = 0;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public void cambiarUnidades(Integer cant) {
        this.cantidad += cant;
    }
}
