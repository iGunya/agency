package com.al.agency.configs.transport.artemis;

import com.al.agency.dto.kafka.TransportMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JmsProducerConfig {

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

        Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
        typeIdMappings.put("JMS_TYPE", TransportMessage.class);

        messageConverter.setTypeIdMappings(typeIdMappings);
        messageConverter.setTypeIdPropertyName("JMS_TYPE");
        messageConverter.setTargetType(MessageType.TEXT);
        return messageConverter;
    }

}
