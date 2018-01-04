package subscribe;

import com.rabbitmq.client.*;
import java.util.*;
import java.util.stream.*;

public class ReceiveLogsByRouting{

	private static final String EXCHANGE_NAME = "direct_logs";
	private static final Set<String> severities = Stream.of("trace","info","warn","error").collect(Collectors.toSet());

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		// Generate a queue with a random name
		String queueName = channel.queueDeclare().getQueue();
		if(args.length<1){
			System.err.println("Usage: ReceiveLogsByRouting [trace][info][warn][error] ");
			System.exit(1);
		}
		for(String severity:args){
			// Binding the created queue with the specified routing key
			channel.queueBind(queueName, EXCHANGE_NAME, severity);
		}
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