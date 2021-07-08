package com.example.agency.repositories;

import com.example.agency.entities.TypeMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMoveRepository extends JpaRepository<TypeMove, Long> {
    TypeMove findByTypeMove(String type);
}
