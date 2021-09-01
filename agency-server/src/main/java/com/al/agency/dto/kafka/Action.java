package com.al.agency.dto.kafka;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Action {
    ADD("добавил"),
    DELETE("удалил"),
    UPDATE("изменил");

    private String string;

    Action(String name){string = name;}

    @Override
    @JsonValue
    public String toString() {
        return string;
    }
}
