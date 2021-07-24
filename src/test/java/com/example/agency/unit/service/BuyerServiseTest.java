package com.example.agency.unit.service;

import com.example.agency.entities.Buyer;
import com.example.agency.entities.Contract;
import com.example.agency.repositories.ContractRepository;
import com.example.agency.services.BuyerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuyerServiseTest {
    @InjectMocks
    private BuyerService buyerService;
    @Mock
    private ContractRepository contractRepository;

    @Test
    public void testSaveBuyer(){
        Buyer buyer = Mockito.mock(Buyer.class);

        buyerService.saveContractAndBuyer(buyer,Mockito.anyString());

        Mockito.verify(contractRepository,Mockito.times(1)).save(Mockito.any(Contract.class));
    }
}
