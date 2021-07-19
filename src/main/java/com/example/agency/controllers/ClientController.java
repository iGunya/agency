package com.example.agency.controllers;

import com.example.agency.entities.Buyer;
import com.example.agency.entities.Seller;
import com.example.agency.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/managers/clients")
public class ClientController {
    private SellerService sellerService;
    private BuyerService buyerService;
    private ContractService contractService;
    private ObjectService objectService;
    private AWSS3ServiceImp awss3Service;

    @GetMapping("/sellers")
    public String getAllSellers(Model model){
        List<Seller> sellers=sellerService.getAllSeller();
        model.addAttribute("sellers",sellers);
        return "all-seller";
    }

    @GetMapping("/buyers")
    public String getAllBuyers(Model model){
        List<Buyer> buyers = buyerService.getAllBuyer();
        model.addAttribute("buyers", buyers);
        return "all-buyer";
    }

    @GetMapping("/sellers/add")
    public String addFormSeller(Model model){
        model.addAttribute("seller",new Seller());
        return "add-seller";
    }

    @GetMapping("/buyers/add")
    public String addFormBayer(Model model){
        model.addAttribute("buyer",new Buyer());
        return "add-buyer";
    }
    @PostMapping("/sellers/add")
    public String saveSeller(@ModelAttribute(value = "seller") Seller seller,
                           @RequestPart(value= "fileName") final MultipartFile multipartFile){
//        String newName = awss3Service.uploadFile(multipartFile);
        String newName =
                "2021-07-16T17:32:57.892523300_test.docx";
        sellerService.saveContractAndSeller(seller,newName);
        return "redirect:/managers/clients/sellers";
    }

    @PostMapping("/buyers/add")
    public String saveBuyer(@ModelAttribute(value = "buyer") Buyer buyer,
                            @RequestPart(value = "fileName") MultipartFile multipartFile){
        //        String newName = awss3Service.uploadFile(multipartFile);
        String newName =
                "2021-07-16T17:32:57.892523300_test.docx";
        if(buyer.getPassport() == null) buyer = buyerService.getBuyerById(buyer.getId_buyer());
        buyerService.saveContractAndBuyer(buyer,newName);
        return "redirect:/managers/clients/buyers";
    }

    @GetMapping("/sellers/contracts")
    public void getAllContractSellers(){

    }
    @GetMapping("/buyers/contracts")
    public void getAllContractBuyers(){
    }

    @Autowired
    public ClientController(SellerService sellerService,
                            BuyerService buyerService,
                            ContractService contractService,
                            ObjectService objectService,
                            AWSS3ServiceImp awss3Service) {
        this.sellerService = sellerService;
        this.buyerService = buyerService;
        this.contractService = contractService;
        this.objectService = objectService;
        this.awss3Service=awss3Service;
    }
}
