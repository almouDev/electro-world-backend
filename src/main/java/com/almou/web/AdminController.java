package com.almou.web;

import com.almou.metier.Article;
import com.almou.metier.Categorie;
import com.almou.metier.ProductImage;
import com.almou.metier.ServiceMetier;
import com.almou.utilities.MainUtils;
import com.almou.utilities.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.almou.utilities.MainUtils.sendError;
import static org.springframework.util.MimeTypeUtils.*;
@AllArgsConstructor
@Controller
@PostAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {
    private ServiceMetier serviceMetier;
    private StorageService storageService;
    @PostMapping("ajout_produit")
    public void ajoutProduit(@RequestParam("files")List<MultipartFile> files,@RequestParam("stock")int stock,@RequestParam("caracteristiques")String caracte, @RequestParam("designation")String designation, @RequestParam("prix") Double prix,@RequestParam("categorie") String categorieName, HttpServletResponse response) {
        Article article=new Article();
        article.setStock(stock);
        article.setDesignation(designation);
        article.setCaracteristiques(caracte);
        article.setPrix(prix);
        Categorie categorie=serviceMetier.findByCategorieName(categorieName);
        if (categorie==null){
            categorie=new Categorie();
            categorie.setCategorieName(categorieName);
            serviceMetier.saveCategorie(categorie);
        }
        article.setCategorie(categorie);
        serviceMetier.saveArticle(article);
        List<ProductImage> images=new ArrayList<>();
        files.stream().filter(multipartFile -> !multipartFile.isEmpty()&&List.of(IMAGE_PNG.toString(),IMAGE_JPEG.toString(),IMAGE_GIF.toString()).contains(multipartFile.getContentType()))
                .forEach(multipartFile -> {
                    try {
                        storageService.saveFile(storageService.uploadDir() + "/" + article.getId(), multipartFile.getOriginalFilename(), multipartFile);
                        ProductImage image = new ProductImage();
                        image.setImage(multipartFile.getOriginalFilename());
                        serviceMetier.saveImage(image);
                        images.add(image);
                    } catch (IOException e) {
                        try {
                            sendError("Une erreur est survenue lors de telechargements de fichiers", HttpStatus.INTERNAL_SERVER_ERROR.value(), response);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
        article.setImage(images);
        serviceMetier.saveArticle(article);
    }
}
