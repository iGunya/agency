package com.al.agency.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_seller;

    @Size(min=6, max=50, message = "Неверный размер поля")
    private String fio;
    @Size(min = 10, max=10)
    private String passport;
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
    private String phone;

//    @OneToMany(mappedBy = "buyer")
//    private List<Contract> contractsBuyer;

    @ManyToMany(mappedBy = "sellers",
                cascade = CascadeType.ALL,
                fetch = FetchType.EAGER)
    private List<Contract> contractsSeller = new ArrayList<>();

    public void addContract(Contract contract) {
        contractsSeller.add(contract);
        contract.getSellers().add(this);
    }

    public void setSeller(Seller seller){
        this.fio = seller.fio;
        this.passport = seller.passport;
        this.phone = seller.phone;
    }
}
