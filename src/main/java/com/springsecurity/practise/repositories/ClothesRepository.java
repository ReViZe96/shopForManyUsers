package com.springsecurity.practise.repositories;

import com.springsecurity.practise.entities.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findAll();

    Optional<Clothes> findById(Long id);

    @Query("select cl from Clothes cl where cl.title =:title")
    List<Clothes> findByTitle(@Param("title") String title);

    @Query("select cl from Clothes cl where cl.title =:title and cl.sex=:sex and cl.description =:description and cl.price=:price")
    Clothes findByTitleSexDescrAndPrice(@Param("title") String title, @Param("sex") String sex,
                                        @Param("description") String description, @Param("price") Integer price);


    void deleteById(Long id);

    @Query("select cl from Clothes cl where cl.sex='Ж'")
    List<Clothes> findWomensClothes();

    @Query("select cl from Clothes cl where cl.sex='М'")
    List<Clothes> findMensClothes();
}