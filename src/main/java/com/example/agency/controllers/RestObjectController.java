package com.example.agency.controllers;

import com.example.agency.dto.ObjectDto;
import com.example.agency.entities.Object;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/managers/objects")
public class RestObjectController {
    @Autowired
    ObjectService objectService;
    @Autowired
    AWSS3ServiceImp awsService;
//    @Autowired
//    private ObjectValidation objectValidation;
//
//    @InitBinder
//    private void initBinder(WebDataBinder binder) {
//        binder.setValidator(objectValidation);
//    }
    @GetMapping("/filter")
    public List<Object> allObject(
                                  @RequestParam(value = "countRoom",required = false) String countRoom,
                                  @RequestParam(value = "maxPrice",required = false) String maxPrice,
                                  @RequestParam(value = "minPrice",required = false) String minPrice,
                                  @RequestParam(value = "typeObject",required = false) String typeObject,
                                  @RequestParam(value = "typeMove",required = false) String typeMove){
        Specification<Object> filter = objectService.createSpecificationForObjects(
                countRoom, maxPrice, minPrice, typeObject, typeMove
        );
        return objectService.getObjectsWithFilter(filter);
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveObject(
                                        @Valid @RequestPart ("object") ObjectDto object,
                                        BindingResult bindingResult,
                                        @RequestPart(value = "fileName[]", required = false) final MultipartFile[] multipartFile
    ){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.OK).body(bindingResult.getFieldErrors());
        }
        List<String> saveFileName = new ArrayList<>();
//        if (!multipartFile[0].getOriginalFilename().equals("")) {
        if (multipartFile != null){
            saveFileName = awsService.uploadFile(multipartFile);
        }else{
            saveFileName.add("2021-07-23T10:41:22.725073500_city.jpg");
            saveFileName.add("2021-07-23T10:41:23.659260200_test.jpg");
        }
        objectService.createObjectAndSavePhotos(object,saveFileName);
        return ResponseEntity.status(HttpStatus.OK).body("Объект добавлен");
    }

    @GetMapping("/delete/{id}")
    public String deleteObject(@PathVariable Long id){
//        objectService.deleteObjectById(id);
        return "Ок";
    }
}
