package com.example.agency.controllers;

import com.example.agency.entities.User;
import com.example.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/only_for_admins",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestAdminController {
    UserService userService;

    @PostMapping("/grand")
    public ResponseEntity<String> grandAccessUser(@RequestBody User user){
        User checkFreeUsername = userService.chekUserByUsername(user.getLogin());
        if(checkFreeUsername==null || checkFreeUsername.getId_user().equals(user.getId_user())){
            userService.updateRoleAndLogin(user);
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь обновлен");
        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пользователь существует");
    }

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }
}
