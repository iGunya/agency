package com.example.agency.dto;

import lombok.Data;

@Data
public class InputObjectDto {
    private Long idObject;

    private String adress;

    private String square;

    private int countFloor;

    private int countRoom;

    private String price;

    private String realPrice;

    private String description;

    private String typeMove;

    private String typeObject;

    private int id_contract;
}
