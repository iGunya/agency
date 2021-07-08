package com.example.agency.repositories;

import com.example.agency.dto.InputObjectDto;
import com.example.agency.entities.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends JpaRepository<Object,Long> {
//    @Modifying
//    @Query(value = "insert into Object o "+"" +
//            "(o.address,o.square,o.count_floor,o.count_room, "+
//            "o.price, o.real_price,o.description,o.type_object,o.type_move) "+
//    "VALUES (:#{#objectDto.adress},:#{#objectDto.square},:#{#objectDto.count_floor},"+
//            ":#{#objectDto.count_room},:#{#objectDto.price},:#{#objectDto.description},"+
//    "(select id_type_object from type_objects t where t.type_object=:#{#objectDto.typeObject}),"+
//    "(select id_type_move from type_moves t where t.type_move=:#{#objectDto.typeMove}))",nativeQuery = true)
//    void save(@Param("") InputObjectDto objectDto);
}
