package com.example.agency.controller;

import com.example.agency.entities.User;
import com.example.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/only_for_admins")
public class AdimistratorController {
    UserService userService;

    @GetMapping
    public String getAllUser(Model model){
        model.addAttribute("users",userService.findAllUser());
        model.addAttribute("userReturn",new User());
        return "admin-user";
    }

    @PostMapping
    public String grandAccessUser(@ModelAttribute User user){
        userService.updateRole(user);
        return "redirect:/only_for_admins";
    }

    @Autowired
    public AdimistratorController(UserService userService) {
        this.userService = userService;
    }
}
