package com.example.sprringIntegrationDemo.service;

import com.example.sprringIntegrationDemo.model.Address;
import com.example.sprringIntegrationDemo.model.Student;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface IntegrationGateway {
    //basic
    @Gateway(requestChannel = "integration.gateway.channel")
    public String sendMessage(String message);

    //Transformer
    @Gateway(requestChannel = "integration.student.gateway.channel")
    public String processStudentDetails(Student student);

    //Router (Payload)
    @Gateway(requestChannel = "router.channel")
    public <T> void process(T object);

    //Router(RecipientList)
    @Gateway(requestChannel = "router.channel.recipient")
    public void processForRecipientList(Student object);

    //Router(Header Value)
    @Gateway(requestChannel = "router.channel.header.value")
    public <T>void processForHeaderValue(T object);

    //Filter
    @Gateway(requestChannel = "router.channel.filter")
    public <T>void processForFilter(T object);
}
