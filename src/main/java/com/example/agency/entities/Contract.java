package com.example.agency.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_contract;

    private String dateSeller;
    private String dateBuyer;
    private String urlContract;

    @ManyToOne(cascade = CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinColumn(name="id_buyer")
    private Buyer buyer;

    @ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinTable( name="contract_seller",
            joinColumns = @JoinColumn(name="id_contract"),
            inverseJoinColumns = @JoinColumn(name = "id_seller"))
    private List<Seller> sellers=new ArrayList<>();

    @Override
    public String toString() {
        return "Contract{" +
                "id_contract=" + id_contract +
                ", dateSeller='" + dateSeller + '\'' +
                ", dateBuyer='" + dateBuyer + '\'' +
                ", urlContract='" + urlContract + '\'' +
                '}';
    }
}
