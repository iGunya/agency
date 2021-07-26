package com.example.agency.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="buyers")
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_buyer;

    @Size(min=6, max=50, message = "Неверный размер поля")
    private String fio;
    @Size(min = 10, max=10)
    private String passport;
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    private String phone;

    private String description;

    @OneToMany(mappedBy = "buyer",fetch = FetchType.EAGER)
    private List<Contract> contracts = new ArrayList<>();

}
