package com.al.agency.controllers;

import com.al.agency.dto.kafka.Action;
import com.al.agency.dto.kafka.KafkaMessage;
import com.al.agency.dto.kafka.ObjectAction;
import com.al.agency.entities.User;
import com.al.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/only_for_admins",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestAdminController {

    private UserService userService;

    @Value("${kafka.topic}")
    private String TOPIC;
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @PostMapping("/grand")
    public ResponseEntity<String> grandAccessUser(@RequestBody User user, Principal principal){
        User checkFreeUsername = userService.chekUserByUsername(user.getLogin());
        if(checkFreeUsername==null || checkFreeUsername.getId_user().equals(user.getId_user())){
            userService.updateRoleAndLogin(user);
            kafkaTemplate.send(TOPIC, new KafkaMessage(principal.getName(), Action.UPDATE, ObjectAction.USER, user.getId_user()));
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь обновлен");
        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь существует");
    }

    @Autowired
    public RestAdminController(UserService userService,
                               KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
    }
}
