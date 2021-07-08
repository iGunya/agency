package com.example.agency.controllers;

import com.example.agency.entities.User;
import com.example.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthenticationController {
    @Autowired
    UserService userService;


    @GetMapping("/registration")
    public String registrationUserPage(Model model){
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrarionUser(@ModelAttribute("user") User user, Model model){
        User exists = userService.findByLogin(user.getLogin());
        if(exists!=null){
            model.addAttribute("login","Такой пользователь существует");
            return "registration";
        }
        userService.save(user);
        return "redirect:/";
    }
}
