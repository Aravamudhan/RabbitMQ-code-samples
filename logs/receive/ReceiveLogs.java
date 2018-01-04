package receive;

import com.rabbitmq.client.*;

public class ReceiveLogs{

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		// Boiler plate starts
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		// Generate a queue with a random name
		String queueName = channel.queueDeclare().getQueue();
		// Binding an exchange and the queue. This makes sure that whatever is pushed into the exchange is received by this queue
		// This enables whatever data that is available in the exchange to be transferred to the queue that is mentioned
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		// Boiler plate ends
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		Consumer consumer = new DefaultConsumer(channel) {
      		@Override
      		public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws java.io.IOException {
        		String message = new String(body, "UTF-8");
        		System.out.println(" [x] Received '" + message + "'");
      		}
    	};
    	channel.basicConsume(queueName, true, consumer);
	}
}