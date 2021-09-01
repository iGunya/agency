package com.al.agency.repositories.specification;

import com.al.agency.entities.Buyer;
import org.springframework.data.jpa.domain.Specification;

public class BuyerSpecification {
    public static Specification<Buyer> fioContains(String substring){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("fio"),"%"+substring+"%");
    }
}
