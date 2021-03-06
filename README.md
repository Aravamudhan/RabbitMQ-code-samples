# Why do we need message queues
* When the result of a task that was performed by user, some operation need to be triggered, and that operation is not part of the 
user's transaction(or does not affect the response received by user) then that is a potential use case for using message queues.
# Some example use cases
* Triggering an email based on the user's action
* Generating computationally heavy reports based on varying inputs of the user 
* Some information that need not be displayed now but must be computed based on the request data
* Bilding a user's timeline in a social media application like twitter (i.e.) whenever someone posts a tweet, the timeline of all the users who follow this person is 
re-built and stored in a cache. Less efficient in terms of storage, but more efficient in terms processing and response time for a user.
# Code samples of RabbitMQ message broker
This repository contains code samples from [RabbitMQ's](https://www.rabbitmq.com/) [tutorial](https://www.rabbitmq.com/getstarted.html)
# How to run
1. [The "hello world"](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/rabbitmq-helloworld/amudhan)
* Open two tabs in a terminal/command line, and goto the helloworld folder in both of them
* To compile the message sender ```javac -classpath "../lib/*:." publisher/Send.java```
* To compile the message receiver ```javac -classpath "../lib/*:." receiver/Receive.java```
* Here the "lib" folder which contains the library files of RabbitMQ and current foler "."(dot) are added to the classpath
* To start the sender ``` java -classpath "../lib/*:." publisher.Send ```
* To start the receiver ```java -classpath "../lib/*:." receiver.Receive ```
* For windows instead of using the color(:) use semi-colon on the class path ```javac -cp "../lib/*;." publisher\Send.java```
2. [Distributing tasks among various workers](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/workqueues)
* Open 3 tabs and navigate to the workqueues folder
* In tab 1 compile the sender/task provider/NewTask ```javac -classpath "../lib/*:." sender/NewTask.java```
* In tab 2 compile the receiver/Worker ```javac -classpath "../lib/*:." receiver/Worker.java```
* Start the NewTask provider ```java -classpath "../lib/*:." sender.NewTask``` and start giving tasks
* The input sample is simply some text like ```Task 1``` or ```Some other task... ```
* For each dot in the input, the worker takes one second(for 3 dots, 3 seconds) to finish. This is to simulate complex tasks that might take more time.
* Start the Worker in tab2 and tab3 by typing```java -classpath "../lib/*:." receiver.Worker```. We are creating 2 workers.
* The tasks would be received in round robin method (i.e.) one to the worker 1(tab2) and other to worker 2 (tab3)
* If a tasks is not completed by one worker then that is passed to another worker. 
3. [Sending messages to many consumers at once](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/messagelogs)
* Goto the folder messagelogs
* This task about sending same message to multiple sources
* Compile the log creator ```javac -classpath "../lib/*:." publish/EmitLogs.java```
* Compile the log receiver in another tab ```javac -classpath "../lib/*:." receive/ReceiveLogs.java```
* Start the log emitter ```java -classpath "../lib/*:." publish.EmitLogs```
* Start a log receiver to receive messages in console ```java -classpath "../lib/*:." receive.ReceiveLogs```
* Start another log receiver to receive messages in a file named rabbitmq_logs.log 
```java -classpath "../lib/*:." receive.ReceiveLogs > rabbitmq_logs.log```
* Now what ever message we type in the log emitter console will be received in the console as well as in the log file
4. [Routing](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/routing)
* When using BuiltinExchangeType.FANOUT, messages are transferred from the exchange to any queue that binds itself with that exchange.
* When using BuiltinExchangeType.DIRECT, messages are transferred from the exchange to queue with the same routing key as this exchange.
* This type of behavior, sending the messages to multiple sources based on the routing key, can be used, for example, in logging the server log messages based on the severity
* Go to the routing folder
* Compile the log creator ```javac -classpath "../lib/*:." publish/EmitLogsByRouting.java```
* Compile the log consumer ```javac -classpath "../lib/*:." subscribe/ReceiveLogsByRouting.java```
* Start a consumer ```java -classpath "../lib/*:." subscribe.ReceiveLogsByRouting trace``` that can receive the logs messages with "trace" level
* This consumer will consume any message with the routing key "trace"
* Start another consumer ```java -classpath "../lib/*:." subscribe.ReceiveLogsByRouting info error``` with log levels info and error
* This consumer will consume any message with the routing keys "info" and "error"
* Start the log emitter ```java -classpath "../lib/*:." publish.EmitLogsByRouting```
* Enter the message and then when the program asks, the log level
* The messages will be delegated to the queues(in our case terminal windows) with matching (routing keys)log levels
5. [Topics](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/topics)
* When using the BuiltinExchangeType.DIRECT, messages are transferred to the matching queues with a routing key. What if we have multiple routing keys ? BuiltinExchangeType.TOPIC solves this problem.
* The routing key in TOPIC exchange is not a single word but multiple words separated by .(dot) or #.
* ```KEY1.*``` in a queue binding can receive messages from an exchange that contains ```KEY1.SOME_WORD``` as a routing key.
* ```#``` queue receives all the messages.
* Go to the topics folder
* Compile the log emitter ```javac -classpath "../lib/*:." publish/EmitByTopics.java```
* Compile the log receiver ```javac -classpath "../lib/*:." subscribe/ReceiveByTopics.java```
* Start the log emitter ```java -classpath "../lib/*:." publish.EmitByTopics```
* In another terminal/terminals start one/multiple log receivers ```java -classpath "../lib/*:." subscribe.ReceiveByTopics "#"``` - Receives all the messages
* ```java -classpath "../lib/*:." subscribe.ReceiveByTopics "critical.#"``` - Receives messages that start with critical and has zero or more words
* This method of exchange is useful when one wants to receive messages in a specific routing key format instead of simply receiving all messages formats with a particular routing key.
