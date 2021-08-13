package com.example.agency.unit.repository;

import com.example.agency.entities.TypeObject;
import com.example.agency.repositories.TypeObjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = "/init_type_object.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
public class TypeObjectRepositoryTest {

    @Autowired
    TypeObjectRepository typeObjectRepository;

    @Test
    public void testFindByTypeObject() {
        TypeObject typeObject = typeObjectRepository.findByTypeObject("Квартира");

        assertThat(typeObject.getIdTypeObject()).isEqualTo(2);
    }

}
