package com.example.agency.repositories;

import com.example.agency.entities.TypeObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeObjectRepository extends JpaRepository<TypeObject,Long> {
    List<TypeObject> findAllBy ();
    TypeObject findByTypeObject(String s);
}
