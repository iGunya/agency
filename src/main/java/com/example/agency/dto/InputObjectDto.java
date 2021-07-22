package com.example.agency.dto;

import com.example.agency.entities.Object;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class InputObjectDto {
    private Long idObject;

    @NotNull(message = "Поле не должно быть пустым")
    private String adress;
    @Pattern(regexp = "(^-?[0-9]*|/+-?[0-9]*)*", message = "Введите в формате \"площадь участка/общая площадь/площадь комнаты...\"")
    private String square;
    @Positive(message = "Поле не может быть отрицательным числом")
    private int countFloor;
    @Positive(message = "Поле не может быть отрицательным числом")
    private int countRoom;
    @Pattern(regexp = "[0-9]+",message = "Неверный формат")
    private String price;
    @Pattern(regexp = "[0-9]+",message = "Неверный формат")
    private String realPrice;

    private String description;

    private String typeMove;

    private String typeObject;

    private int id_contract;

    public void setObject(Object object){
        this.idObject = object.getIdObject();
        this.adress = object.getAdress();
        this.square = object.getSquare();
        this.countFloor = object.getCountFloor();
        this.countRoom = object.getCountRoom();
        this.price = object.getPrice();
        this.realPrice = object.getRealPrice();
        this.description = object.getDescription();
        this.typeObject = object.getTypeObject().getTypeObject();
        this.typeMove = object.getTypeMove().getTypeMove();
    }
}
