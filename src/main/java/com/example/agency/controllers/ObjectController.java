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
    public String allObject(Model model,
                            @RequestParam(value = "countRoom",required = false) String countRoom,
                            @RequestParam(value = "maxPrice",required = false) String maxPrice,
                            @RequestParam(value = "minPrice",required = false) String minPrice,
                            @RequestParam(value = "typeObject",required = false) String typeObject,
                            @RequestParam(value = "typeMove",required = false) String typeMove){
        Specification<Object> filter = objectService.createSpecificationForObjects(
                countRoom, maxPrice, minPrice, typeObject, typeMove
        );
        List<Object> filterObject=objectService.getObjectsWithFilter(filter);
        model.addAttribute("objects",filterObject);
        model.addAttribute("typeObject", objectService.getAllTypeObject());
        model.addAttribute("typeMove", objectService.getAllTypeMove());
        return "main-manager";
    }

    @GetMapping("/add")
    public String addObject(Model model){
        model.addAttribute("object",new ObjectDto());
        model.addAttribute("typeObject", objectService.getAllTypeObject());
        model.addAttribute("typeMove", objectService.getAllTypeMove());
        model.addAttribute("type", "Добавление");
        return "add-object";
    }

    @PostMapping("/add")
    public String saveObject(@ModelAttribute(value = "object") @Valid ObjectDto object,
                             BindingResult bindingResult,
                             @RequestPart(value= "fileName") final MultipartFile[] multipartFile,
                             Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("typeObject", objectService.getAllTypeObject());
            model.addAttribute("typeMove", objectService.getAllTypeMove());
            model.addAttribute("type", object.getIdObject() == null ? "Добавление" : "Обновление");
            return "add-object";
        }
        List<String> saveFileName = new ArrayList<>();
        if (!multipartFile[0].getOriginalFilename().equals("")) {
            saveFileName = awsService.uploadFile(multipartFile);
        }else{
            saveFileName.add("2021-07-23T10:41:22.725073500_city.jpg");
            saveFileName.add("2021-07-23T10:41:23.659260200_test.jpg");
        }
        objectService.createObjectAndSavePhotos(object,saveFileName);
        return "redirect:/managers/objects";
    }

    @GetMapping("/update/{id}")
    public String addObject(@PathVariable Long id,
                            Model model){
        ObjectDto object = new ObjectDto();
        object.setObject(objectService.getObjectById(id));

        model.addAttribute("object",object);
        model.addAttribute("typeObject", objectService.getAllTypeObject());
        model.addAttribute("typeMove", objectService.getAllTypeMove());
        model.addAttribute("type", "Обновление");
        return "add-object";
    }

    @GetMapping("/delete/{id}")
    public String deleteObject(@PathVariable Long id){
        objectService.deleteObjectById(id);
        return "redirect:/managers/objects";
    }
}
