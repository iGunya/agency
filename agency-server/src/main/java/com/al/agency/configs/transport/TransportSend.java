package com.al.agency.configs.transport;

import com.al.agency.dto.kafka.TransportMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransportSend implements Transport {
    @Value("${app.transport}")
    private String nameBean;
    Transport transport;

    @Autowired
    public void setTransport(ApplicationContext context) {
        System.out.println(nameBean);
        this.transport = (Transport) context.getBean(nameBean);
    }

    @Override
    public void send(TransportMessage message) {
        transport.send(message);
    }
}
