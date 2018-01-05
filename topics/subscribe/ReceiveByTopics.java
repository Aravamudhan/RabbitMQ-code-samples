package subscribe;

import com.rabbitmq.client.*;
import java.util.*;

public class ReceiveByTopics{

	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
    	factory.setHost("localhost");
    	Connection connection = factory.newConnection();
    	Channel channel = connection.createChannel();
    	channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
    	String queueName = channel.queueDeclare().getQueue();
		if(args.length<1){
			System.err.println("Usage: ReceiveByTopics routing.key ");
			System.exit(1);
		}
		// Multiple keys can be separated by .(dot)
		// To receive all the messaged use #
		// Example "*.KEY2.*" - Receives all the messages in the format SOMETEXT.KEY2.SOMETEXT
		// "KEY1.#" receives all messages that start with KEY1 and go on for however many keys(like KEY1.SOMETEXT.SOMETEXT.SOMETEXT etc)
		for(String bindingKey : args){
			channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
		}
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		Consumer consumer = new DefaultConsumer(channel) {
      		@Override
      		public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws java.io.IOException {
        		String message = new String(body, "UTF-8");
        		System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
      		}
    	};
    	channel.basicConsume(queueName, true, consumer);

	}
}