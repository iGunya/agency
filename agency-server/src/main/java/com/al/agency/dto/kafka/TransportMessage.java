package com.al.agency.dto.kafka;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportMessage {
    private String username;
    private Action action;
    private ObjectAction objectAction;
    private Long id_objectAction;
}
