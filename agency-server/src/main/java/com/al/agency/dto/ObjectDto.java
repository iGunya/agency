package com.al.agency.dto;

import com.al.agency.entities.Object;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class ObjectDto {
    private Long idObject;

    @Size( min = 1, max = 100, message = "Поле не должно быть пустым")
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
        this.adress = object.getAdress().trim();
        this.square = object.getSquare().trim();
        this.countFloor = object.getCountFloor();
        this.countRoom = object.getCountRoom();
        this.price = object.getPrice().toString();
        this.realPrice = object.getRealPrice().toString();
        this.description = object.getDescription();
        this.typeObject = object.getTypeObject().getTypeObject();
        this.typeMove = object.getTypeMove().getTypeMove();
    }
}
