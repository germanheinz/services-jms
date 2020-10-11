package com.germanheinz.servicesjms.sender;

import com.germanheinz.servicesjms.config.JmsConfig;
import com.germanheinz.servicesjms.model.HelloWorld;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;

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
}
