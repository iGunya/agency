package com.al.agency.repositories;

import com.al.agency.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String login);

    @Transactional
    @Modifying
    @Query("update User u set u.role = :role, u.login = :login where u.id_user = :id")
    void updateRoleAndLogin(@Param("role") String role,
                    @Param("login") String login,
                    @Param("id") Long id);

    List<User> findUserByObjects_IdObject(Long idObject);


}
