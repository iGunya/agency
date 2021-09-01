package com.al.agency.dto.kafka;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ObjectAction {
    OBJECT("объект"),
    BUYER("покупателя"),
    USER("пользователя"),
    SELLER("продавца");

    private String string;

    ObjectAction(String name){string = name;}

    @Override
    @JsonValue
    public String toString() {
        return string;
    }
}
