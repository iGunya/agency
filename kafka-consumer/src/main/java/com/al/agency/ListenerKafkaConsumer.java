package com.al.agency;

import com.al.agency.LogAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ListenerKafkaConsumer {
    @Autowired
    private KafkaTemplate<String, LogAction> kafkaTemplate;
    @Value("${kafka.topic-save}")
    private String TOPIC_SAVE;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}",
    containerFactory = "logKafkaListenerFactory")
    public void listenerTopic(ConsumerRecord<String, LogAction> record) throws JsonProcessingException {
        LogAction message = record.value();

        kafkaTemplate.send(TOPIC_SAVE, message);

        System.out.println("Принято сообщение: топик - " + record.topic() +
            ", партиция - " + record.partition() +
            ", сообщение - " + messageToString(message));

    }

    private String messageToString(LogAction message) {
        return "'Пользователь " + message.getUsername() +
                " " + message.getAction() + " " + message.getObjectAction() +
                " c id " + message.getId_objectAction();
    }

}

//        System.out.println("Принято сообщение: топик - " + record.topic() +
//                ", партиция - " + record.partition() +
//                ", сообщение - 'Пользователь " + message.getUsername() +
//                " " + message.getAction() + " " + message.getObjectAction() + "c id 56'");