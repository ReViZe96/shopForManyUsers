package com.springsecurity.practise.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Clothes> clothes = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    @PrePersist
    public void addCategories() {
        clothes.forEach(clothes -> clothes.getCategories().add(this));
    }

    @PreRemove
    public void removeCategories() {
        clothes.forEach(clothes -> clothes.getCategories().remove(this));
    }
}
