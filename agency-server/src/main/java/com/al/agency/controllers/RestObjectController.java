package com.al.agency.controllers;

import com.al.agency.dto.ObjectDto;
import com.al.agency.dto.kafka.Action;
import com.al.agency.dto.kafka.KafkaMessage;
import com.al.agency.dto.kafka.ObjectAction;
import com.al.agency.entities.Object;
import com.al.agency.services.AWSS3ServiceImp;
import com.al.agency.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/managers/objects")
public class RestObjectController {
    ObjectService objectService;
    AWSS3ServiceImp awsService;

    @Value("${kafka.topic}")
    private String TOPIC;
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

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
                                        @RequestPart(value = "fileName[]", required = false) final MultipartFile[] multipartFile,
                                        Principal principal
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
        Action action = object.getIdObject() == null ? Action.ADD : Action.UPDATE;
        Object objectDB = objectService.createObjectAndSavePhotos(object,saveFileName);
        kafkaTemplate.send(TOPIC, new KafkaMessage(principal.getName(), action, ObjectAction.OBJECT, objectDB.getIdObject()));
        return ResponseEntity.status(HttpStatus.OK).body("Объект добавлен");
    }

    @GetMapping("/delete/{id}")
    public String deleteObject(@PathVariable Long id,
                               Principal principal) {
        objectService.deleteObjectById(id);
        kafkaTemplate.send(TOPIC, new KafkaMessage(principal.getName(), Action.DELETE, ObjectAction.OBJECT, id));
        return "Ок";
    }

    public RestObjectController(ObjectService objectService,
                                AWSS3ServiceImp awsService,
                                KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.objectService = objectService;
        this.awsService = awsService;
        this.kafkaTemplate = kafkaTemplate;
    }
}
