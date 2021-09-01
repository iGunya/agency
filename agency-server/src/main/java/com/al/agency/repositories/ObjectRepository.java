package com.al.agency.repositories;

import com.al.agency.entities.Object;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends PagingAndSortingRepository<Object,Long>, JpaSpecificationExecutor<Object> {
}
