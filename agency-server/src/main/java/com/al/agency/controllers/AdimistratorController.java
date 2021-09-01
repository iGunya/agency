package com.al.agency.controllers;

import com.al.agency.entities.User;
import com.al.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/only_for_admins")
public class AdimistratorController {
    UserService userService;

    @GetMapping
    public String getAllUser(Model model){
        model.addAttribute("users",userService.getAllUser());
        model.addAttribute("userReturn",new User());
        return "admin-user";
    }

    @Autowired
    public AdimistratorController(UserService userService) {
        this.userService = userService;
    }
}
