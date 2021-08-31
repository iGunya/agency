package com.example.agency.repositories.specification;

import com.example.agency.entities.Seller;
import org.springframework.data.jpa.domain.Specification;

public class SellerSpecification {
    public static Specification<Seller> fioContains(String substring){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("fio"),"%"+substring+"%");
    }
}
