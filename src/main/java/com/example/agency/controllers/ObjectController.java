package com.example.agency.controllers;

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
        model.addAttribute("updateObject", new Object());
        model.addAttribute("type", "Добавление");
        return "add-object";
    }
    @PostMapping("/add")
    public String saveObject(@ModelAttribute(value = "object") InputObjectDto objectDto,
                             @RequestPart(value= "fileName") final MultipartFile multipartFile){
        String saveFileName = awsService.uploadFile(multipartFile);
//        String saveFileName =
//                "2021-07-08T19:53:54.557538700_35129.jpg";
        objectService.createObject(objectDto,saveFileName);
        return "redirect:/managers/objects";
    }

    @GetMapping("/update/{id}")
    public String addObject(@PathVariable Long id,
                            Model model){
        Object updateObject = objectService.getObjectById(id);

        model.addAttribute("obgect",new InputObjectDto());
        model.addAttribute("typeObject", objectService.allTypeObject());
        model.addAttribute("typeMove", objectService.allTypeMove());
        model.addAttribute("updateObject", updateObject);
        model.addAttribute("type", "Обновление");
        return "add-object";
    }

    @GetMapping("/delete/{id}")
    public void deleteObject(@PathVariable Long id){
        objectService.delete(id);
    }
}
