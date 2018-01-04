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
2. [Distributing tasks among various workers](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/workqueues)
3. [Sending messages to many consumers at once](https://github.com/Aravamudhan/RabbitMQ-code-samples/tree/master/logs)
