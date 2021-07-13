package com.example.agency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/managers/clients")
public class ClientController {
    @GetMapping("/sellers")
    public void getAllSellers(){
    }

    @GetMapping("/buyers")
    public void getAllBayrs(){
    }

    @GetMapping("/sellers/add")
    public void addFormSeller(){
    }

    @GetMapping("/buyers/add")
    public void addFormBayer(){

    }
    @PostMapping("/sellers/add")
    public void saveSeller(){

    }
    @PostMapping("/buyers/add")
    public void saveBuyer(){

    }
    @GetMapping("/sellers/contracts")
    public void getAllContractSellers(){

    }
    @GetMapping("/buyers/contracts")
    public void getAllContractBuyers(){

    }
}
