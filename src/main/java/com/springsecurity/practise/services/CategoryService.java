package com.springsecurity.practise.services;

import com.springsecurity.practise.entities.Category;
import com.springsecurity.practise.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Transactional
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Transactional
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public void editName(Long id, String newName) {
        Category currCategory = categoryRepository.findById(id).get();
        currCategory.setName(newName);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void save(Category newCategory) {
        categoryRepository.save(newCategory);
    }
}