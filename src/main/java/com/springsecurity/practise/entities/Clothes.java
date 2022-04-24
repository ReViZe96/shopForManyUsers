package com.springsecurity.practise.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clothes")
@Getter
@Setter
@NoArgsConstructor
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String sex;
    private String description;
    private Integer price;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "clothes", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "clothes_category",
            joinColumns = @JoinColumn(name = "clothes_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Clothes(String title, String sex, String description, Integer price) {
        this.title = title;
        this.sex = sex;
        this.description = description;
        this.price = price;
    }

    @PrePersist
    public void addClothes() {
        users.forEach(users -> users.getClothes().add(this));
        categories.forEach(category -> category.getClothes().add(this));
    }

    @PreRemove
    public void removeClothes() {
        users.forEach(users -> users.getClothes().remove(this));
        categories.forEach(category -> category.getClothes().remove(this));
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void deleteCategory(Category category) {
        categories.remove(category);
    }
}