package com.example.agency.repository;

import com.example.agency.entities.TypeObject;
import com.example.agency.repositories.TypeObjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = "/init_type_object.sql")
public class TypeObjectRepositoryTest {

    @Autowired
    TypeObjectRepository typeObjectRepository;

    @Test
    public void testFindByTypeObject() {
        List<TypeObject> objects = typeObjectRepository.findAll();

        TypeObject typeObject = typeObjectRepository.findByTypeObject("Квартира");

        assertThat(typeObject.getIdTypeObject()).isEqualTo(2);
    }

}
