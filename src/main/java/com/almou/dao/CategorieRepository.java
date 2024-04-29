package com.almou.dao;

import com.almou.metier.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    Categorie findByCategorieName(String name);
}
