package com.almou.metier;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ServiceMetier {
    AppUser saveUser(AppUser user);
    AppUser loadByEmail(String email);
    AppRole saveRole(AppRole role);
    List<AppUser> findAllUsers();

    Page<ArticleDTO> listProduits(int page, int size);

    void addRoleToUser(AppUser user, AppRole appRole);
    AppRole addRole(AppRole appRole);

    AppUser findById(Long id);
    Categorie findByCategorieName(String name);
    void saveCategorie(Categorie categorie);

    void saveArticle(Article article1);

    void saveImage(ProductImage image);

    Optional<Article> getProductById(Long id);

    void saveCommande(Commande commande);

    void deleteUser(Long id);
    List<Commande> getAllCommandes();
}

