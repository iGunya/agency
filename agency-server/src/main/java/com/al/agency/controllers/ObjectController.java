package com.al.agency.controllers;

import com.al.agency.dto.ObjectDto;
import com.al.agency.services.AWSS3ServiceImp;
import com.al.agency.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String updateObject(@PathVariable Long id,
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
