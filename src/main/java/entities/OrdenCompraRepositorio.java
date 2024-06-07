package entities;

import java.util.ArrayList;
import java.util.List;

public class OrdenCompraRepositorio {
    private List<OrdenCompra> ordenes;

    public OrdenCompraRepositorio(){
        this.ordenes = new ArrayList<>();
    }

    public void add(OrdenCompra orden){
        this.ordenes.add(orden);
    }

    public List<OrdenCompra> getOrdenes(){
        return this.ordenes;
    }
}
