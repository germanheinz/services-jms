package com.germanheinz.servicesjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.germanheinz.servicesjms.config.JmsConfig;
import com.germanheinz.servicesjms.model.HelloWorld;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        System.out.println("I´m sending a message");

        HelloWorld message = HelloWorld
                .builder()
                .id(UUID.randomUUID())
                .message("Hello world")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
        System.out.println("Message Sent!");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceivingMessage() throws JMSException {
        System.out.println("I´m sending a message");

        HelloWorld message = HelloWorld
                .builder()
                .id(UUID.randomUUID())
                .message("Hello")
                .build();

        Message receiveMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_RECEIVING_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message helloMessage = null;
                try {
                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "com.germanheinz.servicesjms.model.HelloWorld");
                    System.out.println("Sending Hello");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new JMSException("Boom");
                }
            }
        });
        System.out.println(receiveMessage.getBody(String.class));
    }
}
