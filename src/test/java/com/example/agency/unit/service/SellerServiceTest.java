package com.example.agency.unit.service;

import com.example.agency.entities. Seller;
import com.example.agency.entities.Contract;
import com.example.agency.repositories.ContractRepository;
import com.example.agency.services. SellerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SellerServiceTest {
    @InjectMocks
    private  SellerService  SellerService;
    @Mock
    private ContractRepository contractRepository;

    @Test
    public void testSaveSellerAndContract(){
         Seller  Seller = Mockito.mock( Seller.class);

         SellerService.saveContractAndSeller( Seller,Mockito.anyString());

        Mockito.verify(contractRepository,Mockito.times(1)).save(Mockito.any(Contract.class));
    }
}
