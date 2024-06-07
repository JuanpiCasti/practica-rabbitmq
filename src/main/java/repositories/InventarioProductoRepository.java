package repositories;

import entities.InventarioProducto;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventarioProductoRepository {
    private List<InventarioProducto> inventarios = new ArrayList<>();

    public Optional<InventarioProducto> getByNombre(String nombre) {
        Optional<InventarioProducto> inventarioProducto = this.inventarios.stream()
                .filter(inventario -> inventario.getNombre().equals(nombre))
                .findFirst();

        return inventarioProducto;
    }

    public void add(InventarioProducto inventarioProducto) {
        this.inventarios.add(inventarioProducto);
    }

    public void save(InventarioProducto producto) {

    }

    public List<InventarioProducto> getAll() {
        return this.inventarios;
    }
}
