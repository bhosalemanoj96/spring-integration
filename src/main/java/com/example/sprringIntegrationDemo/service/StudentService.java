package com.example.sprringIntegrationDemo.service;

import com.example.sprringIntegrationDemo.model.Student;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class StudentService {

    //Transformers
    @ServiceActivator(inputChannel = "integration.student.objectToJson.channel",outputChannel = "integration.student.jsonToObject.channel")
    public Message<?> receiveMessage(Message<?> message) throws MessagingException{
        System.out.println("-------------------------------");
        System.out.println(message);
        System.out.println("-------------------------------");
        System.out.println("Object to Json- "+message.getPayload());
        return message;
    }

    @ServiceActivator(inputChannel = "integration.student.jsonToObject.fromTransformer.channel")
    public void processJsonToObject(Message<?> message) throws  MessagingException{
        MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        MessageBuilder.fromMessage(message);
        System.out.println("----------------------------------");
        System.out.println("Json to Object - "+message.getPayload());
        Student student = (Student) message.getPayload();
        Message<?> newMessage = MessageBuilder.withPayload(student.toString()).build();
        replyChannel.send(newMessage);
    }

    //Router (Payload)
    @ServiceActivator(inputChannel = "student.channel")
    public void receiveMessageForRouter(Message<?> message) throws MessagingException{
        System.out.println("----------------student.channel--------------");
        System.out.println(message);
        System.out.println("---------------------------------------------");
        System.out.println(message.getPayload());
    }

    //Router (Recipient List)
    @ServiceActivator(inputChannel = "student.channel.1")
    public void receiveMessageForRecipientListRouter1(Message<?> message) throws MessagingException{
        System.out.println("----------------student.channel.1--------------");
        System.out.println(message);
        System.out.println("---------------------------------------------");
        System.out.println(message.getPayload());
    }

    @ServiceActivator(inputChannel = "student.channel.2")
    public void receiveMessageForRecipientListRouter2(Message<?> message) throws MessagingException{
        System.out.println("----------------student.channel.2--------------");
        System.out.println(message);
        System.out.println("---------------------------------------------");
        System.out.println(message.getPayload());
    }
}
