package com.example.agency.controllers;

import com.example.agency.entities.User;
import com.example.agency.services.UserService;
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

//    @PostMapping
//    public String grandAccessUser(@ModelAttribute("user") User user,
//                                        Model model){
//        User checkFreeUsername = userService.chekUserByUsername(user.getLogin());
//        if(checkFreeUsername==null || checkFreeUsername.getId_user().equals(user.getId_user())){
//            userService.updateRoleAndLogin(user);
//            return "redirect:/only_for_admins";
//        }else
//            model.addAttribute("error","Пользователь с таким именем существет");
//        model.addAttribute("users",userService.getAllUser());
//        model.addAttribute("userReturn",new User());
//        return "admin-user";
//    }

    @Autowired
    public AdimistratorController(UserService userService) {
        this.userService = userService;
    }
}
