package com.almou.metier;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(types = {Article.class},name = "proj1")
interface ArticleProjection1 {
    Long getId();
    double getPrix();
    double getOprix();
    String getDesignation();
    Categorie getCategorie();
    String getCaracteristiques();
    List<ProductImage> getImage();
}
@Projection(types ={Article.class},name = "proj2")
interface ArticleProjection2{
    Long getId();
    String getDesignation();
}