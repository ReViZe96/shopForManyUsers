package com.springsecurity.practise.controllers;

import com.springsecurity.practise.entities.Category;
import com.springsecurity.practise.entities.Clothes;
import com.springsecurity.practise.entities.User;
import com.springsecurity.practise.services.CategoryService;
import com.springsecurity.practise.services.ClothesService;
import com.springsecurity.practise.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class ShopController {
    private ClothesService clothesService;
    private CategoryService categoryService;
    private UserService userService;

    @GetMapping("/mainPage")
    public String getMainPage() {
        return "mainPage";
    }

    @GetMapping("/catalog")
    public String getCatalogPage(Model model) {
        List<Clothes> allClothes = clothesService.findAll();
        model.addAttribute("allClothes", allClothes);
        return "catalog";
    }

    @GetMapping("/categories")
    public String getCategoryPage(Model model) {
        List<Category> allCategories = categoryService.findAll();
        model.addAttribute("allCategories", allCategories);
        return "categories";
    }

    @PostMapping("/search")
    public String getSearchPage(@RequestParam String title, Model model) {
        List<Clothes> foundClothes = clothesService.findByTitle(title);
        Integer amount = foundClothes.size();
        model.addAttribute("foundClothes", foundClothes);
        model.addAttribute("amount", amount);
        return "resultOfSearch";
    }

    @GetMapping("/womensCloth")
    public String getWomenClothPage(Model model) {
        List<Clothes> womensClothes = clothesService.findWomensClothes();
        model.addAttribute("womensClothes", womensClothes);
        return "womensClothes";
    }

    @GetMapping("/mensCloth")
    public String getMensClothPage(Model model) {
        List<Clothes> mensClothes = clothesService.findMensClothes();
        model.addAttribute("mensClothes", mensClothes);
        return "mensClothes";
    }

    @GetMapping("/showCurrentClothe/{id}")
    public String showCurrentClothe(@PathVariable("id") Long id, Model model) {
        Clothes currCloth = clothesService.findById(id);
        Set<Category> currCategories = currCloth.getCategories();
        List<User> currUsers = currCloth.getUsers();
        model.addAttribute("currCategories", currCategories);
        model.addAttribute("currUsers", currUsers);
        model.addAttribute("currCloth", currCloth);
        return "currentCloth";
    }

    @GetMapping("/showCurrentCategory/{id}")
    public String showCurrentCategory(@PathVariable("id") Long id, Model model) {
        Category currCategory = categoryService.findById(id);
        Set<Clothes> currClothes = currCategory.getClothes();
        model.addAttribute("currClothes", currClothes);
        model.addAttribute("currCategory", currCategory);
        return "currentCategory";
    }

    @PostMapping("/buyCurrentClothe/{id}")
    public String makeBuy(@PathVariable("id") Long id, Principal principal) {
        User currUser = userService.getCurrentUser(principal);
        Clothes currClothe = clothesService.findById(id);
        userService.saveCloth(currUser, currClothe);
        return "buySuccess";
    }
}