package com.springsecurity.practise.controllers;

import com.springsecurity.practise.entities.Category;
import com.springsecurity.practise.entities.Clothes;
import com.springsecurity.practise.entities.Role;
import com.springsecurity.practise.entities.User;
import com.springsecurity.practise.services.CategoryService;
import com.springsecurity.practise.services.ClothesService;
import com.springsecurity.practise.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private ClothesService clothesService;
    private CategoryService categoryService;

    @Secured("ROLE_ADMIN") //защита на уровне метода (можно и так)
    @RequestMapping("/deleteCloth/{id}")
    public String deleteCloth(@PathVariable("id") Long id) {
        clothesService.delete(id);
        return "redirect:/catalog";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/addCloth")
    public String getAddingPage() {
        return "addCloth";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/addCloth")
    public String addCloth(@RequestParam String title, @RequestParam String category, @RequestParam String sex,
                           @RequestParam String description, @RequestParam Integer price) {
        Category searchedCategory = categoryService.findByName(category);
        Clothes newCloth = new Clothes(title, sex, description, price);
        newCloth.getCategories().add(searchedCategory);
        Clothes equalCloth = clothesService.findEqual(title, sex, description, price);
        if (!(equalCloth == null) || searchedCategory == null) {
            return "failedAddingCloth";
        } else {
            clothesService.save(newCloth);
            return "redirect:/catalog";
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editTitle/{id}")
    public String editTitle(@PathVariable("id") Long id, @RequestParam String title) {
        clothesService.setTitle(id, title);
        return "redirect:/showCurrentClothe/{id}";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable("id") Long id, @RequestParam String name, Model model) {
        Category searchedCategory = categoryService.findByName(name);
        Clothes currClothes = clothesService.findById(id);
        Set<Category> curCategories = currClothes.getCategories();
        String path = null;
        if (!(searchedCategory == null)) {
            boolean categoryAlreadyAdd = false;
            for (Category curr : curCategories) {
                if (curr.equals(searchedCategory)) {
                    path = "categoryAlreadyAdd";
                    categoryAlreadyAdd = true;
                    break;
                }
            }
            if (!categoryAlreadyAdd) {
                clothesService.addCategory(id, searchedCategory);
                Clothes updClothes = clothesService.findById(id);
                model.addAttribute("currCloth", updClothes);
                Set<Category> updCategoties = updClothes.getCategories();
                model.addAttribute("currCategories", updCategoties);
                List<User> users = updClothes.getUsers();
                model.addAttribute("currUsers", users);
                path = "currentCloth";
            }
        } else {
            path = "categoryNotExist";
        }
        return path;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editSex/{id}")
    public String editSex(@PathVariable("id") Long id, @RequestParam String sex) {
        clothesService.setSex(id, sex);
        return "redirect:/showCurrentClothe/{id}";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editDescription/{id}")
    public String editDescription(@PathVariable("id") Long id, @RequestParam String description) {
        clothesService.setDescription(id, description);
        return "redirect:/showCurrentClothe/{id}";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editPrice/{id}")
    public String editPrice(@PathVariable("id") Long id, @RequestParam Integer price) {
        clothesService.setPrice(id, price);
        return "redirect:/showCurrentClothe/{id}";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editCategoryName/{id}")
    public String editCategoryName(@PathVariable("id") Long id, @RequestParam String name) {
        categoryService.editName(id, name);
        return "redirect:/categories";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/deleteCategoryForThis/{categoryId}/{clothesId}")
    public String deleteCategoryForThis(@PathVariable("categoryId") Long categoryId, @PathVariable("clothesId") Long clothesId) {
        clothesService.deleteCurrCategory(categoryId, clothesId);
        return "redirect:/showCurrentClothe/{clothesId}";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/addNewCategory")
    public String addCategory(@RequestParam String name) {
        Category newCategory = new Category(name);
        Category equalCategory = categoryService.findByName(name);
        if (!(equalCategory == null)) {
            return "failedAddingCategory";
        } else {
            categoryService.save(newCategory);
            return "redirect:/categories";
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/allUsers")
    public String getUserListPage(Model model, Principal principal) {
        List<User> usersList = userService.findAll();
        User loggedUser = userService.findByUsername(principal.getName());
        usersList.remove(loggedUser);
        model.addAttribute("usersList", usersList);
        return "allUsers";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/showCurrentUser/{id}")
    public String showCurrentUser(@PathVariable("id") Long id, Model model) {
        User currUser = userService.findById(id);
        Collection<Role> currRoles = currUser.getAuthorities();
        Collection<Clothes> currClothes = currUser.getClothes();
        model.addAttribute("currRoles", currRoles);
        model.addAttribute("currUser", currUser);
        model.addAttribute("currClothes", currClothes);
        model.addAttribute("Admin", "ROLE_ADMIN");
        model.addAttribute("User", "ROLE_USER");
        /*При данной реализации приложения список возможных ролей можно изменять только путём модификации миграций ->
        -> при каждом подобном изменении, ТУТ необходимо добавлять в модель вновь появившиеся роли */
        return "currentUser";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/deleteCurrentRoleForThis/{roleId}/{userId}")
    public String deleteRoleForThis(@PathVariable("roleId") Long roleId, @PathVariable("userId") Long userId) {
        userService.deleteCurrRole(roleId, userId);
        return "redirect:/admin/showCurrentUser/{userId}";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/addUsersRoles/{id}")
    public String addUsersRole(@PathVariable("id") Long id, @RequestParam String role) {
        userService.addRole(id, role);
        return "redirect:/admin/showCurrentUser/{id}";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/allUsers";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/findByRole")
    public String findByRole(@RequestParam String role, Model model) {
        List<User> foundUsers = userService.findByRole(role);
        Integer amount = foundUsers.size();
        model.addAttribute("foundUsers", foundUsers);
        model.addAttribute("amount", amount);
        return "usersByRole";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/findByUsername")
    public String findByUsername(@RequestParam String username, Model model) {
        User foundUser = userService.findByUsername(username);
        model.addAttribute("foundUser", foundUser);
        return "userByName";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/findByEmail")
    public String findByEmail(@RequestParam String email, Model model) {
        User foundUser = userService.findByEmail(email);
        model.addAttribute("foundUser", foundUser);
        return "userByEmail";
    }
}