package com.example.agency.entities;

import com.example.agency.dto.InputObjectDto;
import lombok.Data;

import javax.persistence.*;
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

    private String price;

    private String realPrice;

    private String description;

    @ManyToOne
    @JoinColumn(name="id_type_object")
    private TypeObject typeObject;

    @ManyToOne
    @JoinColumn(name="id_type_move")
    private TypeMove typeMove;

    @OneToMany(mappedBy = "objects",fetch= FetchType.LAZY)
    private List<Photo> photos;

    private Integer idContract;

    public Object(){}

    public Object(InputObjectDto objectDto) {
        this.adress = objectDto.getAdress();
        this.square = objectDto.getSquare();
        this.countFloor = objectDto.getCountFloor();
        this.countRoom = objectDto.getCountRoom();
        this.price = objectDto.getPrice();
        this.realPrice = objectDto.getRealPrice();
        this.description = objectDto.getDescription();
    }
}
