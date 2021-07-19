package com.example.agency.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="buyers")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_buyer;

    private String fio;

    private String passport;

    private String description;

    @OneToMany(mappedBy = "buyer")
    private List<Contract> contracts = new ArrayList<>();

}
