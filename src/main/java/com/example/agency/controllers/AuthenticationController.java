package com.example.agency.controllers;

import com.example.agency.entities.User;
import com.example.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {
    @Autowired
    UserService userService;


    @GetMapping("/registration")
    public String registrationUserPage(Model model){
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @GetMapping("/after_login")
    public String afterLogin(HttpServletRequest request){
        if(request.isUserInRole("ROLE_ADMIN")){
            return "redirect:/only_for_admins";
        }
        if(request.isUserInRole("ROLE_USER")){
            return "successful-registration";
        }
        return "redirect:/managers/objects";
    }

    @PostMapping("/registration")
    public String registrarionUser(@ModelAttribute("user") User user, Model model){
        User chekFreeUsername = userService.chekUserByUsername(user.getLogin());
        if(chekFreeUsername != null){
            model.addAttribute("login","Такой пользователь существует");
            return "registration";
        }
        userService.saveNewUser(user);
        model.addAttribute("error","Вы успешно зарегистрированны");
        return "login";
    }
}
