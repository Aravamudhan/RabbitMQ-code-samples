package publish;

import com.rabbitmq.client.*;
import java.util.Scanner;

/**
** This is to check sending the message to multiple receivers
** This publishes messages to an exchange not to a queue and from that exchange messages are added to queues
**/
public class EmitLogs{

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		// Boiler plate starts
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		// Boiler plate ends
		Scanner input = new Scanner(System.in);
		String message = "";
		do{
			System.out.println("Enter the message to be logged (enter q to exit): ");
			message = input.nextLine();
			if(!message.equals("q")){
				// No queue name, only an exchange name
				channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
				System.out.println(" [x] Sent '" + message + "'");
			}
		}while(!message.equals("q"));
		input.close();
		channel.close();
		connection.close();

	}
}