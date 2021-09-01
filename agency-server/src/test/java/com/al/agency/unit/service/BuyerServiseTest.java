package com.al.agency.unit.service;

import com.al.agency.entities.Buyer;
import com.al.agency.repositories.BuyerRepository;
import com.al.agency.repositories.ContractRepository;
import com.al.agency.services.BuyerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BuyerServiseTest {
    @InjectMocks
    private BuyerService buyerService;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private BuyerRepository buyerRepository;

    @Test
    public void testSaveNewBuyer(){
        Buyer buyer = Mockito.mock(Buyer.class);
        String NAME_SAVE_FILE = "name file";

        Mockito.when(buyer.getId_buyer()).thenReturn(null);

        buyerService.saveContractAndBuyer(buyer, NAME_SAVE_FILE);

        Mockito.verify(buyerRepository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(buyerRepository,Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    public void testUpdateBuyer(){
        Buyer buyer = Mockito.mock(Buyer.class);
        String NAME_SAVE_FILE = "name file";

        Mockito.when(buyer.getId_buyer()).thenReturn(1L);
        Mockito.doReturn(Optional.of(buyer)).when(buyerRepository).findById(Mockito.anyLong());

        buyerService.saveContractAndBuyer(buyer, NAME_SAVE_FILE);

        Mockito.verify(buyerRepository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(buyerRepository,Mockito.times(1)).findById(Mockito.anyLong());
    }
}
