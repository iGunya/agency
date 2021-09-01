package com.al.agency.entities;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer",fetch = FetchType.EAGER)
    private List<Contract> contracts = new ArrayList<>();

    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.setBuyer(this);
    }

    public void setBuyer(Buyer buyer){
        this.fio = buyer.fio;
        this.passport = buyer.passport;
        this.phone = buyer.phone;
        this.description = buyer.description;
    }
}
