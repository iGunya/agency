package com.al.agency.repository;

import com.al.agency.entites.LogAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogActionRepository extends JpaRepository<LogAction, Long> {
}
