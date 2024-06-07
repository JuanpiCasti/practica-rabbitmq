package entities;



import java.util.ArrayList;
import java.util.List;

public class OrdenCompra {
    private String nombre;
    private List<Producto> productos;

    public OrdenCompra(String nombre){
        this.nombre = nombre;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String getNombre() {
        return nombre;
    }
}
