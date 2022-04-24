package com.springsecurity.practise.controllers;

import com.springsecurity.practise.entities.User;
import com.springsecurity.practise.repositories.RoleRepository;
import com.springsecurity.practise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//Контроллер регистрации
@Controller
public class RegistrationController {
    private UserService userService;

    private RoleRepository roleRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/registrationForm")
    public String getRegistrationPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User user, Model model) {
        user.getAuthorities().add(roleRepository.findByName("ROLE_USER"));
        boolean res = userService.saveUser(user);
        if (!res) {
            model.addAttribute("usernameExist", "Пользователь с таким логином и/или почтой уже существует.");
            return "registration";
        }
        return "mainPage";
    }
}