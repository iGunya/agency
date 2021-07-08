package com.example.agency.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/")
    public String allObject(){
        return "hello";
    }
    @GetMapping("/{id}")
    public void getObgect(){

    }
    @GetMapping("/{id}/photos")
    public void getObgectPhoto(){

    }
    @GetMapping("/like/{id_user}}")
    public void getLikeObgect(){

    }
}
