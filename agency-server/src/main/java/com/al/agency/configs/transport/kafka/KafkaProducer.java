package com.al.agency.configs.transport.kafka;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements Transport {
    @Autowired
    public KafkaTemplate<String, TransportMessage> kafkaTemplate;
    @Value("${kafka.topic}")
    private String TOPIC;

    @Override
    public void send(TransportMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
