package com.example.agency.services;

import com.example.agency.entities.Buyer;
import com.example.agency.entities.Contract;
import com.example.agency.repositories.BuyerRepository;
import com.example.agency.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BuyerService {
    private BuyerRepository buyerRepository;
    private ContractRepository contractRepository;

    public List<Buyer> getBuyerWithPaginationAndFilter(Specification<Buyer> specification){
        return buyerRepository.findAll(specification);
    }

    public void saveContractAndBuyer(Buyer buyer, String nameFile){
        Contract contract = new Contract();
        contract.setUrlContract(nameFile);
        contract.setDateBuyer(getCurrentDate());

        contract.setBuyer(buyer);

        contractRepository.save(contract);
    }

    public Buyer getBuyerById(Long id){
        return buyerRepository.findById(id).orElse(null);
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    @Autowired
    public BuyerService(BuyerRepository buyerRepository, ContractRepository contractRepository) {
        this.buyerRepository = buyerRepository;
        this.contractRepository = contractRepository;
    }
}
