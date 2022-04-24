package com.springsecurity.practise.services;

import com.springsecurity.practise.entities.Category;
import com.springsecurity.practise.entities.Clothes;
import com.springsecurity.practise.repositories.CategoryRepository;
import com.springsecurity.practise.repositories.ClothesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ClothesService {
    private ClothesRepository clothesRepository;
    private CategoryRepository categoryRepository;

    @Transactional
    public List<Clothes> findAll() {
        return clothesRepository.findAll();
    }

    @Transactional
    public Clothes findById(Long id) {
        return clothesRepository.findById(id).get();
    }

    @Transactional
    public List<Clothes> findByTitle(String title) {
        return clothesRepository.findByTitle(title);
    }

    @Transactional
    public Clothes findEqual(String title, String sex, String description, Integer price) {
        return clothesRepository.findByTitleSexDescrAndPrice(title, sex, description, price);
    }

    @Transactional
    public List<Clothes> findWomensClothes() {
        return clothesRepository.findWomensClothes();
    }

    @Transactional
    public List<Clothes> findMensClothes() {
        return clothesRepository.findMensClothes();
    }

    @Transactional
    public void setTitle(Long id, String newTitle) {
        Clothes curClothes = clothesRepository.findById(id).get();
        curClothes.setTitle(newTitle);
    }

    @Transactional
    public void setSex(Long id, String newSex) {
        Clothes curClothes = clothesRepository.findById(id).get();
        curClothes.setSex(newSex);
    }

    @Transactional
    public void setDescription(Long id, String newDescription) {
        Clothes curClothes = clothesRepository.findById(id).get();
        curClothes.setDescription(newDescription);
    }

    @Transactional
    public void setPrice(Long id, Integer newPrice) {
        Clothes curClothes = clothesRepository.findById(id).get();
        curClothes.setPrice(newPrice);
    }

    @Transactional
    public void addCategory(Long id, Category category) {
        Clothes curClothes = clothesRepository.findById(id).get();
        curClothes.addCategory(category);
    }

    @Transactional
    public void deleteCurrCategory(Long categoryId, Long clothesId) {
        Category currCategory = categoryRepository.findById(categoryId).get();
        Clothes currCloth = clothesRepository.findById(clothesId).get();
        currCloth.deleteCategory(currCategory);

    }

    @Transactional
    public void delete(Long id) {
        clothesRepository.deleteById(id);
    }

    @Transactional
    public void save(Clothes newCloth) {
        clothesRepository.save(newCloth);
    }
}