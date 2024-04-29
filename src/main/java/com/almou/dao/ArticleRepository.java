package com.almou.dao;

import com.almou.metier.Article;
import com.almou.metier.Categorie;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
public interface ArticleRepository extends JpaRepository<Article, Long>, PagingAndSortingRepository<Article, Long> {
    @RestResource(path = "designation")
    @Query("from  produits where designation like %:titre%")
    List<Article> findByDesignation(@Param("titre") String titre);
    @RestResource(path = "categorie")
    @Query("from produits where categorie.categorieName like %:nom%")
    List<Article> findByCategorie(@Param("nom")String nom, Pageable page);
}
