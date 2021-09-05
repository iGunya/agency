package com.al.agency.configs.transport;

import com.al.agency.dto.kafka.TransportMessage;

public interface Transport {
    void send(TransportMessage message);
}
