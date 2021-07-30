package com.example.agency.unit.service;

import com.example.agency.entities. Seller;
import com.example.agency.entities.Contract;
import com.example.agency.repositories.ContractRepository;
import com.example.agency.repositories.SellerRepository;
import com.example.agency.services. SellerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SellerServiceTest {
    @InjectMocks
    private  SellerService  sellerService;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private SellerRepository sellerRepository;

    @Test
    public void testSaveSellerAndContract(){
         Seller Seller = Mockito.mock(Seller.class);

         sellerService.saveContractAndSeller( Seller,Mockito.anyString());

        Mockito.verify(contractRepository,Mockito.times(1)).save(Mockito.any(Contract.class));
    }

    @Test
    public void testAddContractForSeller(){
        Long idContract = 1L;
        Long idSeller = 1L;

        Mockito.when(contractRepository.findById(idContract)).thenReturn(Optional.of(new Contract()));
        Mockito.when(sellerRepository.findById(idSeller)).thenReturn(Optional.of(new Seller()));

        boolean ans = sellerService.addContractForSellerById(idContract,idSeller);

        Mockito.verify(contractRepository,Mockito.times(1)).save(Mockito.any(Contract.class));
        Assert.assertTrue(ans);
    }

    @Test
    public void testAddBadContractForSeller(){
        Long idContract = 1L;
        Long idSeller = 1L;

        Mockito.doReturn(Optional.empty()).when(contractRepository).findById(Mockito.anyLong());

        boolean ans = sellerService.addContractForSellerById(idContract,idSeller);

        Mockito.verify(contractRepository,Mockito.times(0)).save(Mockito.any(Contract.class));
        Assert.assertFalse(ans);
    }
}
