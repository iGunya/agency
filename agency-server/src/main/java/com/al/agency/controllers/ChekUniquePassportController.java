package com.al.agency.controllers;

import com.al.agency.services.BuyerService;
import com.al.agency.services.SellerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/managers/clients")
public class ChekUniquePassportController {

    private SellerService sellerService;
    private BuyerService buyerService;

    @GetMapping("/seller/{passport}")
    public String checkPassportSeller(@PathVariable String passport) {
        if (sellerService.checkPassport(passport)) {
            return "not unique";
        } else {
            return "unique";
        }
    }

    @GetMapping("/buyer/{passport}")
    public String checkPassportBuyer(@PathVariable String passport) {
        if (buyerService.checkPassport(passport)) {
            return "not unique";
        } else {
            return "unique";
        }
    }

    public ChekUniquePassportController(SellerService sellerService, BuyerService buyerService) {
        this.sellerService = sellerService;
        this.buyerService = buyerService;
    }
}
