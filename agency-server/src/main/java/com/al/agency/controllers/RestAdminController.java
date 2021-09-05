package com.al.agency.controllers;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.Action;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.dto.kafka.ObjectAction;
import com.al.agency.entities.User;
import com.al.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

@RestController
@RequestMapping(value = "/only_for_admins",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestAdminController {

    private UserService userService;
    Transport transportSend;

    @Value("${kafka.topic}")
    private String TOPIC;
    @Value("${app.transport}")
    private String nameBean;



    @PostMapping("/grand")
    public ResponseEntity<String> grandAccessUser(@RequestBody User user, Principal principal){
        User checkFreeUsername = userService.chekUserByUsername(user.getLogin());
        if(checkFreeUsername==null || checkFreeUsername.getId_user().equals(user.getId_user())){
            userService.updateRoleAndLogin(user);
            transportSend.send(new TransportMessage(principal.getName(), Action.UPDATE, ObjectAction.USER, user.getId_user()));
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь обновлен");
        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь существует");
    }

    @Autowired
    public RestAdminController(UserService userService, Transport transportSend) {
        this.userService = userService;
        this.transportSend = transportSend;
    }
}
