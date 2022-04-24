package com.springsecurity.practise.controllers;

import com.springsecurity.practise.entities.Clothes;
import com.springsecurity.practise.entities.User;
import com.springsecurity.practise.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Secured("ROLE_USER")
    @GetMapping("/profile")
    public String getProfilePage(Model model, Principal principal) {
        User currUser = userService.getCurrentUser(principal);
        List<Clothes> currClothes = currUser.getClothes();
        model.addAttribute("currUser", currUser);
        model.addAttribute("currClothes", currClothes);
        return "profile";
    }

    @Secured("ROLE_USER")
    @PostMapping("/editUsername/{id}")
    public String editUsername(@PathVariable("id") Long id, @RequestParam String username, Model model) {
        List<User> allUsers = userService.findAll();
        String path = null;
        boolean nameExist = false;
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUsername().equals(username)) {
                path = "usernameAlreadyExist";
                nameExist = true;
                break;
            }
        }
        if (!nameExist) {
            userService.setUsername(id, username);
            model.addAttribute("username", "username");
            path = "successChanged";
        }
        return path;
    }

    @Secured("ROLE_USER")
    @PostMapping("/editEmail/{id}")
    public String editEmail(@PathVariable("id") Long id, @RequestParam String email, Model model) {
        List<User> allUsers = userService.findAll();
        String path = null;
        boolean mailExist = false;
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getEmail().equals(email)) {
                path = "emailAlreadyExist";
                mailExist = true;
                break;
            }
        }
        if (!mailExist) {
            userService.setEmail(id, email);
            User updUser = userService.findById(id);
            model.addAttribute("currUser", updUser);
            List<Clothes> currClothes = updUser.getClothes();
            model.addAttribute("currClothes", currClothes);
            path = "profile";
        }
        return path;
    }

    @Secured("ROLE_USER")
    @PostMapping("/editPassword/{id}")
    public String editPassword(@PathVariable("id") Long id, @RequestParam String password, Model model) {
        userService.setPassword(id, password);
        model.addAttribute("password", "password");
        return "successChanged";
    }
}