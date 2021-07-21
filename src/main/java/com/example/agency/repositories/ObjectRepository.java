package com.example.agency.repositories;

import com.example.agency.entities.Object;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends PagingAndSortingRepository<Object,Long>, JpaSpecificationExecutor<Object> {
}
