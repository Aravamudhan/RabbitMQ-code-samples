package publish;

import com.rabbitmq.client.*;
import java.util.*;
import java.util.stream.*;

public class EmitLogsByRouting{

	private static final String EXCHANGE_NAME = "direct_logs";
	private static final Set<String> severities = Stream.of("trace","info","warn","error").collect(Collectors.toSet());

	public static void main(String[] args) throws Exception {
		// Boiler plate starts
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// The messages will be transferred to a queue that was bound with this exchange using the same routing key
		// In this class the routing keys are selected from the set "EmitLogsByRouting.severities"
		// When a queue is created and bound to this exchange named "EmitLogsByRouting.EXCHANGE_NAME", that should 
		// use at least one of the keys to receive the messages
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		// Boiler plate ends
		Scanner input = new Scanner(System.in);
		String message = "";
		do{
			System.out.println("Enter the message to be logged (enter q to exit): ");
			message = input.nextLine();
			System.out.println("Enter the severity (trace,info,warn,error):");
			String severity = input.nextLine();
			if(!message.equals("q") && severities.contains(severity)){
				// No queue name, only an exchange name and routing key
				// The routing key is set according to the user input
				channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
				System.out.println(" [x] Sent '" + message + "'");
			}
		}while(!message.equals("q"));
		input.close();
		channel.close();
		connection.close();
	}
}