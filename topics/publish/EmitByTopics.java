package publish;

import com.rabbitmq.client.*;
import java.util.*;

/**
* Send messages to exchange based list of keywords delimited by (dot).
* This is done by based on TOPIC exchange method
* This is useful when we want to send messages based on multiple criteria not just a single one like DIRECT exchange method
**/
public class EmitByTopics{

	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // Exchange type is topic
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Scanner input = new Scanner(System.in);
		String message = "";
		do{
			System.out.println("Enter the message to be logged (enter q to exit): ");
			message = input.nextLine();
			if(!message.equals("q")){
				System.out.println("Enter the routingKey :");
				// The examples of routing keys
				// KEY1 - queues with binding key "KEY1" will get this message
				// SOMEWORD.KEY2.SOMEWORD -  queues with binding key *.KEY2.* will receive this
				// SOMEWORD. SOMEWORD.KEY3 - queues with binding key *.*.KEY3 will receive this
				// SOMEWORD.SOMEOTHERWORDS - queues with "#" receive all the message. Just act like DIRECT exchange
				String routingKey = input.nextLine();
				channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
				System.out.println(" [x] Sent '" + routingKey+" : "+message + "'");
			}
		}while(!message.equals("q"));
		input.close();
		channel.close();
		connection.close();

	}
}