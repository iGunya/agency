package com.example.agency.controllers;

import com.example.agency.dto.ObjectDto;
import com.example.agency.entities.Object;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
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
        getTypes(model);
        return "main-manager";
    }

    @GetMapping("/add")
    public String addObject(Model model){
        model.addAttribute("object",new ObjectDto());
        getTypes(model);
        model.addAttribute("type", "Добавление");
        return "add-object";
    }

    @GetMapping("/update/{id}")
    public String addObject(@PathVariable Long id,
                            Model model){
        ObjectDto object = new ObjectDto();
        object.setObject(objectService.getObjectById(id));

        model.addAttribute("object",object);
        getTypes(model);
        model.addAttribute("type", "Обновление");
        return "add-object";
    }

    private void getTypes(Model model){
        model.addAttribute("typeObject", objectService.getAllTypeObject());
        model.addAttribute("typeMove", objectService.getAllTypeMove());
    }
}
