package com.al.agency.services;

import com.al.agency.entities.Buyer;
import com.al.agency.entities.Contract;
import com.al.agency.repositories.BuyerRepository;
import com.al.agency.repositories.ContractRepository;
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

    public List<Buyer> getBuyersWithFilter(Specification<Buyer> specification){
        return buyerRepository.findAll(specification);
    }

    public Buyer saveContractAndBuyer(Buyer buyer, String nameFile){
        Contract contract = new Contract();
        contract.setUrlContract(nameFile);
        contract.setDateBuyer(getCurrentDate());

        Buyer buyerDB;
        if (buyer.getId_buyer() != null) {
            buyerDB = buyerRepository.findById(buyer.getId_buyer()).orElse(null);
        } else{
            buyerDB = new Buyer();
        }

        if(buyer.getPassport() != null)
            buyerDB.setBuyer(buyer);

        buyerDB.addContract(contract);
        return buyerRepository.save(buyerDB);
    }

    public Buyer getBuyerById(Long id){
        return buyerRepository.findById(id).orElse(null);
    }

    public void deleteBuyerById(Long id){
        buyerRepository.deleteById(id);
    }

    public boolean checkPassport(String passport){
        return buyerRepository.countByPassport(passport) != 0;
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
