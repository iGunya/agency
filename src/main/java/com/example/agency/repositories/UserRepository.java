package com.example.agency.repositories;

import com.example.agency.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String login);

    @Transactional
    @Modifying
    @Query("update User u set u.role = :role where u.id_user = :id")
    void updateRole(@Param("role") String role,
                    @Param("id") Long id);

}
