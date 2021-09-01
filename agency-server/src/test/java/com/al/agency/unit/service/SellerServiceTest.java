package com.al.agency.unit.service;

import com.al.agency.entities.Contract;
import com.al.agency.entities.Seller;
import com.al.agency.repositories.ContractRepository;
import com.al.agency.repositories.SellerRepository;
import com.al.agency.services.SellerService;
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
        Seller seller = Mockito.mock(Seller.class);
        String NAME_SAVE_FILE = "name file";

        Mockito.when(seller.getId_seller()).thenReturn(null);

        sellerService.saveContractAndSeller(seller, NAME_SAVE_FILE);

        Mockito.verify(sellerRepository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(sellerRepository,Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    public void testUpdateSeller(){
        Seller seller = Mockito.mock(Seller.class);
        String NAME_SAVE_FILE = "name file";

        Mockito.when(seller.getId_seller()).thenReturn(1L);
        Mockito.doReturn(Optional.of(seller)).when(sellerRepository).findById(Mockito.anyLong());

        sellerService.saveContractAndSeller(seller, NAME_SAVE_FILE);

        Mockito.verify(sellerRepository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(sellerRepository,Mockito.times(1)).findById(Mockito.anyLong());
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
