package receiver;

import com.rabbitmq.client.*;

public class Worker {

	private final static String QUEUE_NAME = "WorkerTaskQueue";
	private final static boolean autoAck = false;
	private final static boolean durable = true;// Even if the server crashes, the queue will not be lost
	// Tell RabbitMQ that at any time, only one message should be handed over to a worker/receiver
	// This makes sure that workers are not bombarded with tasks
	private final static int prefetchCount = 1; 

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
    	Connection connection = factory.newConnection();
    	Channel channel = connection.createChannel();
    	channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
    	channel.basicQos(prefetchCount);
    	System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    	Consumer consumer = new DefaultConsumer(channel) {
      		@Override
      		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          		throws java.io.IOException {
        		String message = new String(body, "UTF-8");
        		System.out.println(" [x] Received '" + message + "'");
        		try{
        			doWork(message);
        		} finally{
        			System.out.println(" [x] Done");
        			channel.basicAck(envelope.getDeliveryTag(), false);
        		}
      		}
    	};
    	channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}

	private static void doWork(String task) {
    	for (char ch: task.toCharArray()) {
        	if (ch == '.') {
        		try{
        			Thread.sleep(1000);
        		} catch(InterruptedException _ignored){
    	    		Thread.currentThread().interrupt();
	        	}
        	} 
    	}
	}    
}