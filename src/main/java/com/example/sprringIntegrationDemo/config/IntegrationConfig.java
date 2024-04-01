package com.example.sprringIntegrationDemo.config;

import com.example.sprringIntegrationDemo.model.Address;
import com.example.sprringIntegrationDemo.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSelector;
import org.springframework.integration.filter.MessageFilter;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.HeaderValueMessageProcessor;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfig {

    //basic
    @Bean
    public MessageChannel receiverChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel replyChannel(){
        return new DirectChannel();
    }

    //Transformer
    @Bean
    @Transformer(inputChannel = "integration.student.toConvertObject.channel", outputChannel = "integration.student.objectToJson.channel")
    public ObjectToJsonTransformer objectToJsonTransformer(){
        return new ObjectToJsonTransformer(getMapper());
    }

    public Jackson2JsonObjectMapper getMapper(){
        ObjectMapper mapper = new ObjectMapper();
        return new Jackson2JsonObjectMapper(mapper);
    }

    @Bean
    @Transformer(inputChannel = "integration.student.jsonToObject.channel", outputChannel = "integration.student.jsonToObject.fromTransformer.channel")
    public JsonToObjectTransformer jsonToObjectTransformer(){
        return new JsonToObjectTransformer(Student.class);
    }

    //HeaderEnricher
    @Bean
    @Transformer(inputChannel = "integration.student.gateway.channel",
    outputChannel = "integration.student.toConvertObject.channel")
    public HeaderEnricher enrichHeader(){
        Map<String, HeaderValueMessageProcessor<String>> headerToAdd = new HashMap<>();
        headerToAdd.put("header1",new StaticHeaderValueMessageProcessor<String>("Test Header 1"));
        headerToAdd.put("header2",new StaticHeaderValueMessageProcessor<String>("Test Header 2"));
        HeaderEnricher enricher = new HeaderEnricher(headerToAdd);
        return enricher;
    }

    //Router (Payload)
    @Bean
    @ServiceActivator(inputChannel = "router.channel")
    public PayloadTypeRouter routerPayload(){
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(Student.class.getName(),"student.channel");
        router.setChannelMapping(Address.class.getName(),"address.channel");
        return router;
    }
    //Router (Recipient List)
    @Bean
    @ServiceActivator(inputChannel = "router.channel.recipient")
    public RecipientListRouter routerRecipientList(){
        RecipientListRouter router = new RecipientListRouter();
        router.addRecipient("student.channel.1");
        router.addRecipient("student.channel.2");
        return router;
    }

    //Router (Header Value)
    @Bean
    @ServiceActivator(inputChannel = "router.channel.header.value")
    public PayloadTypeRouter routerPayloadForHeaderValue(){
        PayloadTypeRouter router = new PayloadTypeRouter();
        router.setChannelMapping(Student.class.getName(),"student.enrich.header.channel");
        router.setChannelMapping(Address.class.getName(),"address.enrich.header.channel");
        return router;
    }

    @Bean
    @Transformer(inputChannel = "student.enrich.header.channel",outputChannel = "header.payload.router.channel")
    public HeaderEnricher enrichHeaderForStudent(){
        Map<String, HeaderValueMessageProcessor<String>> headerToAdd = new HashMap<>();
        headerToAdd.put("testHeader",new StaticHeaderValueMessageProcessor<String>("student"));
        HeaderEnricher enricher = new HeaderEnricher(headerToAdd);
        return enricher;
    }

    @Bean
    @Transformer(inputChannel = "address.enrich.header.channel", outputChannel = "header.payload.router.channel")
    public HeaderEnricher enrichHeaderForAddress(){
        Map<String, HeaderValueMessageProcessor<String>> headerToAdd = new HashMap<>();
        headerToAdd.put("testHeader",new StaticHeaderValueMessageProcessor<String>("address"));
        HeaderEnricher enricher = new HeaderEnricher(headerToAdd);
        return enricher;
    }

    @Bean
    @ServiceActivator(inputChannel = "header.payload.router.channel")
    public HeaderValueRouter headerRouter(){
        HeaderValueRouter router = new HeaderValueRouter("testHeader");
        router.setChannelMapping("student","student.channel");
        router.setChannelMapping("address","address.channel");
        return router;
    }
    @Bean
    @Filter(inputChannel = "router.channel.filter")
    public MessageFilter filter(){
        MessageFilter filter = new MessageFilter(new MessageSelector() {
            @Override
            public boolean accept(Message<?> message) {
                return message.getPayload() instanceof Student;
            }
        });
        filter.setOutputChannelName("student.channel");
        return filter;
    }
}
