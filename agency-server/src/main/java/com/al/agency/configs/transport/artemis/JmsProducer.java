package com.al.agency.configs.transport.artemis;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer implements Transport {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void send(TransportMessage message) {
        jmsTemplate.convertAndSend("MoveUsers", message);
    }
}
