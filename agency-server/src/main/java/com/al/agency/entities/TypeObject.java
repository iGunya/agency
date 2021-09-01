package com.al.agency.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="type_objects")
public class TypeObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeObject;

    private String typeObject;

//    @OneToMany(mappedBy = "typeObject",fetch= FetchType.LAZY)
//    private List<Object> objects;
}
