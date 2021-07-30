package com.example.agency.entities;

import com.example.agency.dto.ObjectDto;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="objects")
public class Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObject;

    private String adress;

    private String square;

    private int countFloor;

    private int countRoom;

    private Long price;

    private Long realPrice;

    private String description;

    @ManyToOne
    @JoinColumn(name="id_type_object")
    private TypeObject typeObject;

    @ManyToOne
    @JoinColumn(name="id_type_move")
    private TypeMove typeMove;

    @OneToMany(fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "id_object")
    private List<Photo> photos = new ArrayList<>();

    private Integer idContract;

    public Object(){}

    public void setObjectDto(ObjectDto objectDto){
        this.adress = objectDto.getAdress();
        this.square = objectDto.getSquare();
        this.countFloor = objectDto.getCountFloor();
        this.countRoom = objectDto.getCountRoom();
        this.price = Long.parseLong(objectDto.getPrice());
        this.realPrice = Long.parseLong(objectDto.getRealPrice());
        this.description = objectDto.getDescription();
    }
}
