package com.al.agency.service;

import com.al.agency.entites.LogAction;
import com.al.agency.repository.LogActionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListenerKafkaConsumer {

    @Autowired
    private LogActionRepository logActionRepository;


    @KafkaListener(topics = "${kafka.topic-save}", groupId = "${kafka.group-id}",
            containerFactory = "logKafkaListenerFactory")
    public void listenerTopic(ConsumerRecord<String, LogAction> record) {
        LogAction message = record.value();

        System.out.println("Принято сообщение: топик - " + record.topic() +
                ", партиция - " + record.partition() +
                ", сообщение - " + messageToString(message));

        logActionRepository.save(message);
        System.out.println("Сообщение схраннено");
    }

    public List<String> getLog(){
        return logActionRepository.findAll()
                .stream()
                .map(this::messageToString)
                .collect(Collectors.toList());
    }

    private String messageToString(LogAction message) {
        return "'Пользователь " + message.getUsername() +
                " " + message.getAction() + " " + message.getObjectAction() +
                " c id " + message.getId_objectAction();
    }

}