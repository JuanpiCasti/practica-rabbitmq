package Rabbit;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import entities.OrdenCompra;
import entities.OrdenCompraRepositorio;
import entities.Producto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GestionadorOrdenes {
    // Sacaria en un .properties o env

    private final static String QUEUE_NAME = "hello";
    private final static String HOST = "localhost";
    private final static String EXCHANGE_NAME = "nuestro_exchange";
    static OrdenCompraRepositorio ordenes = new OrdenCompraRepositorio();


    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();

        System.out.println(queueName); // quiero ver que es esto
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("⏱️ Esperando mensajes :)");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(" [x] Done" );
            }
        };


        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        String[] parts = task.split(" ");
        String orderName = parts[0];
        List<String> productNames = new ArrayList<>(Arrays.asList(parts).subList(1,parts.length));
        OrdenCompra orden = new OrdenCompra(orderName);
        for (int i = 0; i < productNames.size(); i++) {
            orden.agregarProducto(new Producto(productNames.get(i)));
        }

        ordenes.add(orden);

        ordenes.getOrdenes().stream().forEach(o -> {
            System.out.println("Orden: " + o.getNombre());
            o.getProductos().forEach(producto -> {
                System.out.println("Producto: " + producto.getNombre());
            });
        });


    }
}