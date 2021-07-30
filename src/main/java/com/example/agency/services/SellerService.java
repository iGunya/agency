package com.example.agency.services;

import com.example.agency.entities.Contract;
import com.example.agency.entities.Object;
import com.example.agency.entities.Seller;
import com.example.agency.repositories.ContractRepository;
import com.example.agency.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SellerService {
    private SellerRepository sellerRepository;
    private ContractRepository contractRepository;

    public List<Seller> getSellerWithPaginationAndFilter(Specification<Seller> specification){
        return sellerRepository.findAll(specification);
    }

    public void saveContractAndSeller(Seller seller,String nameFile){
        Contract contract = new Contract();
        contract.setUrlContract(nameFile);
        contract.setDateSeller(getCurrentDate());

        contract.getSellers().add(seller);

        contractRepository.save(contract);
    }

    public Seller getSellerById(Long id){
        return sellerRepository.findById(id).orElse(null);
    }

    public boolean addContractForSellerById(Long idContract, Long idSeller){
        Contract contract = contractRepository.findById(idContract).orElse(null);
        if (contract == null)
            return false;
        Seller seller = sellerRepository.findById(idSeller).orElse(null);
        contract.getSellers().add(seller);

        contractRepository.save(contract);
        return true;
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    @Autowired
    public SellerService(SellerRepository sellerRepository,
                         ContractRepository contractRepository) {
        this.sellerRepository = sellerRepository;
        this.contractRepository = contractRepository;
    }
}
