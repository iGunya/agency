package com.example.agency.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.agency.entities.User;
import com.example.agency.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@Sql(scripts = "/init_users.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByLoginUser() {
        List<User> userList = userRepository.findAll();
        User user = userRepository.findByLogin("manager");

        assertThat(user.getLogin()).isEqualTo("manager");
    }

    @Test
    public void testUpdateLoginAndRoleUser() {
        Long id_user=4L;

        userRepository.updateRoleAndLogin("ROLE_MANAGER","newAdmin",id_user);

        User newUser = userRepository.findByLogin("newAdmin");

        assertThat(newUser.getRole()).isEqualTo("ROLE_MANAGER");
        assertThat(newUser.getId_user()).isEqualTo(id_user);
    }

}
