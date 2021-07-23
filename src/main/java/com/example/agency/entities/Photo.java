package com.example.agency.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPhoto;

    private String URL_photo;

//    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
//    @JoinColumn(name = "id_object")
//    private Object objects;
}