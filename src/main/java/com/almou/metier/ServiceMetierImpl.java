package com.almou.metier;

import com.almou.dao.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Service
@Transactional
public class ServiceMetierImpl implements ServiceMetier {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CommandeRepository commandeRepository;
    private ArticleRepository articleRepository;
    private CategorieRepository categorieRepository;
    private ImageRepository imageRepository;
    @Override
    public  AppUser loadByEmail(String email){
        return userRepository.findByEmail(email);
    }
    @Override
    public AppUser saveUser(AppUser user){
        PasswordEncoder passwordEncoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String pwd=passwordEncoder.encode(user.getPassword());
        user.setPassword(pwd);
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveCommande(Commande commande) {
        commande=commandeRepository.save(commande);
    }

    @Override
	public AppRole saveRole(AppRole role) {
		roleRepository.save(role);
		return role;
	}

    @Override
    public List<AppUser> findAllUsers() {
        return (List<AppUser>) userRepository.findAll();
    }

    @Override
    public Page<ArticleDTO> listProduits(int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Page<ArticleDTO> articlePage=articleRepository.findAll(pageable)
                .map(article -> ArticleDTO.builder()
                        .prix(article.getPrix())
                        .designation(article.getDesignation())
                        .id(article.getId())
                        .images(article.getImage()).build());
        return  articlePage;
    }

    @Override
    public void addRoleToUser(AppUser user, AppRole appRole) {
        user.getUserRoles().add(appRole);
        userRepository.save(user);
    }

    @Override
    public AppRole addRole(AppRole appRole) {
        roleRepository.save(appRole);
        return appRole;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Commande> getAllCommandes() {
        return (List<Commande>) commandeRepository.findAll();
    }

    @Override
    public AppUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Categorie findByCategorieName(String name) {
        return
                categorieRepository.findByCategorieName(name);
    }
    @Override
    public void saveCategorie(Categorie categorie) {
        categorieRepository.save(categorie);
    }

    @Override
    public void saveArticle(Article article1) {
        articleRepository.save(article1);
    }
    @Override
    public void saveImage(ProductImage image){imageRepository.save(image);}

    @Override
    public Optional<Article> getProductById(Long id) {
        return articleRepository.findById(id);
    }
}
