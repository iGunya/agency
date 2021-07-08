package com.example.agency.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="type_moves")
public class TypeMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeMove;

    private String typeMove;

//    @OneToMany(mappedBy = "typeMove",fetch = FetchType.LAZY)
//    private List<Object> objects;
}
