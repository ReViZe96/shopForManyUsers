package com.springsecurity.practise.controllers;

import com.springsecurity.practise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//контроллер авторизации
@Controller
public class LogController {
    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout, Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }
}