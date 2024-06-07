package Rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import entities.InventarioProducto;
import repositories.InventarioProductoRepository;

import java.util.List;
import java.util.Optional;

public class MicroservicioInventario {

    private final static String HOST = "localhost";
    private final static String EXCHANGE_NAME = "nuestro_exchange";

    public static void main(String[] argv) throws Exception {
        InventarioProductoRepository repo = new InventarioProductoRepository();

        InventarioProducto cocacola = new InventarioProducto("CocaCola");
        cocacola.cambiarUnidades(10);
        InventarioProducto pepsi = new InventarioProducto("Pepsi");
        pepsi.cambiarUnidades(10);
        InventarioProducto fanta = new InventarioProducto("Fanta");
        fanta.cambiarUnidades(10);

        repo.add(cocacola);
        repo.add(pepsi);
        repo.add(fanta);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("Servicio de inventario 📜");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            List<String> orden = List.of(message.split(" "));
            String nombreOrden = orden.get(0);
            List<String> productos = orden.subList(1, orden.size());

            System.out.println(" [✅] Recibida orden '" + nombreOrden + "'");

            registrarBajasStock(productos, repo);

        };


        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private static void registrarBajasStock(List<String> productos, InventarioProductoRepository repo) {

        productos.forEach(producto -> {

            // Estaria mejor primero ver si todos los productos existen para saber si rechazar la orden antes de
            // hacer cambios en el stock

            Optional<InventarioProducto> productoBuscado = repo.getByNombre(producto);

            if (productoBuscado.isPresent()) {

                productoBuscado.get().cambiarUnidades(-1);

            } else {

                System.out.println("No se encontro el producto " + producto);

            }

        });

        // Print all product stocks
        repo.getAll().forEach(producto -> {
            System.out.println(producto.getNombre() + ": " + producto.getCantidad());
        });
    }

}
