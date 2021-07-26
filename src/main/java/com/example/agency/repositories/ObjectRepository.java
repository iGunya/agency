package com.example.agency.repositories;

import com.example.agency.entities.Object;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectRepository extends PagingAndSortingRepository<Object,Long>, JpaSpecificationExecutor<Object> {
    @Query("select o from Object o where ty  < :maxPrice")
    List<Object> lesser(Long maxPrice);
}
