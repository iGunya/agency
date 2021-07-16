package com.example.agency.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_seller;

    private String fio;

    private String passport;

//    @OneToMany(mappedBy = "buyer")
//    private List<Contract> contractsBuyer;

    @ManyToMany(mappedBy = "sellers",
            cascade = {
                    CascadeType.ALL
            })
    private List<Contract> contractsSeller = new ArrayList<>();
}
