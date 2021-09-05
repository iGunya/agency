package com.al;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class JmsConsumer {
    @Autowired
    private KafkaTemplate<String, LogAction> kafkaTemplate;
    @Value("${kafka.topic-save}")
    private String TOPIC_SAVE;

    @JmsListener(destination = "MoveUsers", containerFactory="jsaFactory")
    public void listenerQueue(LogAction message) {

        kafkaTemplate.send(TOPIC_SAVE, message);

        System.out.println(messageToString(message));
    }

    private String messageToString(LogAction message) {
        return "Пользователь " + message.getUsername() +
                " " + message.getAction() + " " + message.getObjectAction() +
                " c id " + message.getId_objectAction();
    }
}
