package com.example.agency.repositories;

import com.example.agency.entities.Seller;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends PagingAndSortingRepository<Seller,Long>, JpaSpecificationExecutor<Seller> {
}
