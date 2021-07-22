package com.example.agency.controllers;

import com.example.agency.dto.InputObjectDto;
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
                            @RequestParam(value = "countRoom",required = false) Integer countRoom,
                            @RequestParam(value = "maxPrice",required = false) String maxPrice,
                            @RequestParam(value = "minPrice",required = false) String minPrice,
                            @RequestParam(value = "typeObject",required = false) String typeObject,
                            @RequestParam(value = "typeMove",required = false) String typeMove){
        Specification<Object> filter = objectService.createSpecificationForObject(
                countRoom, maxPrice, minPrice, typeObject, typeMove
        );
        List<Object> filterObject=objectService.getObjectWithPaginationAndFilter(filter);
        model.addAttribute("objects",filterObject);
        model.addAttribute("typeObject", objectService.allTypeObject());
        model.addAttribute("typeMove", objectService.allTypeMove());
        return "main-manager";
    }

    @GetMapping("/add")
    public String addObject(Model model){
        model.addAttribute("object",new InputObjectDto());
        model.addAttribute("typeObject", objectService.allTypeObject());
        model.addAttribute("typeMove", objectService.allTypeMove());
        model.addAttribute("type", "Добавление");
        return "add-object";
    }
    @PostMapping("/add")
    public String saveObject(@ModelAttribute(value = "object") @Valid InputObjectDto object,
                             BindingResult bindingResult,
                             @RequestPart(value= "fileName") final MultipartFile multipartFile,
                             Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("typeObject", objectService.allTypeObject());
            model.addAttribute("typeMove", objectService.allTypeMove());
            model.addAttribute("type", object.getIdObject() == null ? "Добавление" : "Обновление");
            return "add-object";
        }
        String saveFileName = null;
        if (multipartFile.getName().equals("")) saveFileName = awsService.uploadFile(multipartFile);
//        String saveFileName =
//                "2021-07-08T19:53:54.557538700_35129.jpg";
        objectService.createObject(object,saveFileName);
        return "redirect:/managers/objects";
    }

    @GetMapping("/update/{id}")
    public String addObject(@PathVariable Long id,
                            Model model){
        InputObjectDto object = new InputObjectDto();
        object.setObject(objectService.getObjectById(id));

        model.addAttribute("object",object);
        model.addAttribute("typeObject", objectService.allTypeObject());
        model.addAttribute("typeMove", objectService.allTypeMove());
        model.addAttribute("type", "Обновление");
        return "add-object";
    }

    @GetMapping("/delete/{id}")
    public void deleteObject(@PathVariable Long id){
        objectService.delete(id);
    }
}
