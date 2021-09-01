package com.al.agency.repositories;

import com.al.agency.entities.Buyer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends PagingAndSortingRepository<Buyer,Long>, JpaSpecificationExecutor<Buyer> {
}
