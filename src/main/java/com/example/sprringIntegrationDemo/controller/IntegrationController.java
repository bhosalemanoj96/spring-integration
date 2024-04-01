package com.example.sprringIntegrationDemo.controller;

import com.example.sprringIntegrationDemo.model.Address;
import com.example.sprringIntegrationDemo.model.Student;
import com.example.sprringIntegrationDemo.service.IntegrationGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrate")
public class IntegrationController {

    //basic
    @Autowired
    private IntegrationGateway integrationGateway;

    @GetMapping(value = "{name}")
    public String getMessageFromIntegrationService(@PathVariable("name") String name){
        return integrationGateway.sendMessage(name);
    }

    //Transformer
    @PostMapping("/transform")
    public String processStudentDetails(@RequestBody Student student){
        return integrationGateway.processStudentDetails(student);
    }

    //Router(Payload)
    @PostMapping("/student")
    public void processRoutingStudentDetails(@RequestBody Student student){
        integrationGateway.process(student);
    }

    @PostMapping("/address")
    public void processRoutingAddressDetails(@RequestBody Address address){
        integrationGateway.process(address);
    }

    //Router(Recipient List)
    @PostMapping("/student/multiple/recipients")
    public void processRoutingRecipientListStudentDetails(@RequestBody Student student){
        integrationGateway.processForRecipientList(student);
    }

    //Router(Header Value)
    @PostMapping("/route/header/value/student")
    public void processRoutingHeaderValueStudentDetails(@RequestBody Student student){
        integrationGateway.processForHeaderValue(student);
    }

    @PostMapping("/route/header/value/address")
    public void processRoutingHeaderValueAddressDetails(@RequestBody Address address){
        integrationGateway.processForHeaderValue(address);
    }

    //Filter
    @PostMapping("/filterProcess/student")
    public void processFilteringStudentDetails(@RequestBody Student student){
        integrationGateway.processForFilter(student);
    }

    @PostMapping("/filterProcess/address")
    public void processFilteringStudentDetails(@RequestBody Address address){
        integrationGateway.processForFilter(address);
    }
}
