package com.example.agency.repositories.specification;

import com.example.agency.entities.Object;
import com.example.agency.entities.TypeMove;
import com.example.agency.entities.TypeObject;
import org.springframework.data.jpa.domain.Specification;

public class ObjectSpecification {
    public static Specification<Object> countRoomEqual(int countRoom){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("countRoom"),countRoom);
    }

    public static Specification<Object> priceGreaterThanOrEqual(String maxPrice){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"),maxPrice);
    }
    public static Specification<Object> priceLesserThanOrEqual(String minPrice){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice);
    }
    public static Specification<Object> typeObjectEqual(TypeObject typeObject){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("typeObject"),typeObject);
    }
    public static Specification<Object> typeMoveEqual(TypeMove typeMove){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("typeMove"),typeMove);
    }
}
