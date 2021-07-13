package com.example.agency.controller;

import com.example.agency.dto.InputObjectDto;
import com.example.agency.entities.Object;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/managers/objects")
public class ObjectController {
    @Autowired
    ObjectService objectService;
    @Autowired
    AWSS3ServiceImp awsService;

    @GetMapping
    public String allObject(Model model){
        List<Object> allObject=objectService.getAllObject();
        model.addAttribute("objects",allObject);
        return "main-manager";
    }

    @GetMapping("/add")
    public String addObject(Model model){
        model.addAttribute("obgect",new InputObjectDto());
        model.addAttribute("typeObject", objectService.allTypeObject());
        model.addAttribute("typeMove", objectService.allTypeMove());
        return "add-object";
    }
    @PostMapping("/add")
    public String saveObject(@ModelAttribute(value = "object") InputObjectDto objectDto,
                             @RequestPart(value= "fileName") final MultipartFile multipartFile){
        String saveFileName = awsService.uploadFile(multipartFile);
        objectService.createObject(objectDto,saveFileName);
        return "redirect:/managers/objects";
    }

    @GetMapping("/delete")
    public void deleteObject(){

    }
}
