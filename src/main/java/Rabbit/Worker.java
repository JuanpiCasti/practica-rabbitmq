package Rabbit;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class Worker {
    // Sacaria en un .properties o env

    private final static String QUEUE_NAME = "hello";
    private final static String HOST = "localhost";
    private final static String EXCHANGE_NAME = "nuestro_exchange";

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
                System.out.println(" [x] Done");
            }
        };


        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {

                if (ch == '.') Thread.sleep(1000);

        }
    }
}
