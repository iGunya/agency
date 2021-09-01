package com.al.agency.controller;

import com.al.agency.service.ListenerKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestControllerLog {
    @Autowired
    private ListenerKafkaConsumer listenerKafkaConsumer;

    @GetMapping
    public List<String> getLog(){
        return listenerKafkaConsumer.getLog();
    }
}
