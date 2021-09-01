package com.al.agency.repositories;

import com.al.agency.entities.TypeObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeObjectRepository extends JpaRepository<TypeObject,Long> {
    TypeObject findByTypeObject(String s);
}
