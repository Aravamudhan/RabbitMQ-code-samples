package sender;

import com.rabbitmq.client.*;
import java.util.Scanner;

public class NewTask{

	private final static String QUEUE_NAME = "WorkerTaskQueue";
	private final static boolean durable = true;

	public static void main(String[] argv) throws Exception {
		// Boiler plate starts
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// If any of the queue parameter change, we have to create a new queue by changing the name
		// An already existing queue, will throw an error
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		// Boiler plate ends
		Scanner input = new Scanner(System.in);
		String task = "";
		do{
			System.out.println("Enter the task name (enter q to exit): ");
			task = input.nextLine();
			if(!task.equals("q")){
				// MessageProperties.PERSISTENT_TEXT_PLAIN - To make sure that the messages are not lost and saved to the disk
				channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, task.getBytes());
				System.out.println(" [x] Sent '" + task + "'");
			}
		}while(!task.equals("q"));
		input.close();
		channel.close();
		connection.close();
	}

}