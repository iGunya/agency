package com.example.agency.unit.repository;

import com.example.agency.entities.TypeMove;
import com.example.agency.repositories.TypeMoveRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = "/init_type_move.sql")
public class TypeMoveRepositoryTest {
    @Autowired
    private TypeMoveRepository typeMoveRepository;

    @Test
    public void testFindByLoginUser() {
        TypeMove typeMove = typeMoveRepository.findByTypeMove("Продажа");

        assertThat(typeMove.getIdTypeMove()).isEqualTo(1);
    }


}
