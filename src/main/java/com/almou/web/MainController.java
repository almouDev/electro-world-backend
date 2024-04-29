package com.almou.web;

import com.almou.dao.CommandeRepository;
import com.almou.dao.ProductQuantityRepository;
import com.almou.metier.*;
import com.almou.utilities.JwtUtilities;
import com.almou.utilities.MainUtils;
import com.almou.utilities.StorageService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
@RestController
@AllArgsConstructor
public class MainController {
    private ServiceMetier serviceMetier;
    private StorageService storageService;
    private ProductQuantityRepository quantityRepository;
    private CommandeRepository commandeRepository;
    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorization_token=request.getHeader("Authorization");
        if (authorization_token!=null&&authorization_token.startsWith("Bearer ")){
            try{
                String refresh_token=authorization_token.substring(7);
                DecodedJWT decodedJWT=JwtUtilities.JwtTokenDecryption(authorization_token);
                AppUser appUser=serviceMetier.loadByEmail(decodedJWT.getSubject());
                Collection<String> roles=appUser.getUserRoles().stream().map(r->r.getName()).collect(Collectors.toList());
                String access_token=JwtUtilities.JwtTokenCreation(request.getRequestURL().toString(),15*60*1000,roles,decodedJWT.getSubject()).sign(JwtUtilities.getAlgorithm());
                JwtUtilities.SendTokens(access_token,refresh_token,response);
            }catch (Exception e){
                MainUtils.sendError("Votre jéton d'authentification est soit revoqué soit invalid", HttpStatus.FORBIDDEN.value(), response);
            }
        }else {
            MainUtils.sendError("Votre jéton d'authentification est soit revoqué soit invalid", HttpStatus.FORBIDDEN.value(), response);
        }
    }
    @GetMapping("/products/images/{productId}/{filename}")
    public byte[] downloadImage(HttpServletResponse response,@PathVariable("productId")String productId,@PathVariable("filename")String filename) throws IOException {
        try {
            String uploadDir=System.getProperty("user.dir")+"/src/main/resources/static/";
            return storageService.downloadFile(uploadDir+productId,filename);
        } catch (IOException e) {
            response.sendError(500,"File download failed");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/utilisateurs/{username}")
    public AppUser getUser(@PathVariable("username") String username){
        return serviceMetier.loadByEmail(username);
    }
    @PostMapping("/commander")
    public Commande commander(@RequestBody Commande commande){
        double totalCmd=0;
        for (ProductQuantity productQuantity:commande.getArticles()){
            productQuantity.setCommande(commande);
            totalCmd+=productQuantity.getQuantity()*productQuantity.getArticle().getPrix();
        }
        commande.setTotal(totalCmd);
        commande.setDate_commande(new Date());
        commande.setShipped(false);
        serviceMetier.saveCommande(commande);
        return commande;
    }
    @GetMapping("/ventes/annuel")
    public List<RevenuParCategorie> getAnnuellesVentes(){
        List<RevenuParCategorie> parCategories=new ArrayList<>();
        for (Object[] o:quantityRepository.getRevenuAnnuel()){
            parCategories.add(RevenuParCategorie.builder()
                    .categorie((String) o[0])
                    .revenu((Double) o[1])
                    .build());
        }
        return parCategories;
    }
    @GetMapping("/ventes/mensuel")
    public List<RevenuParCategorie> getMensuelVentes(@RequestParam(value = "categorie",defaultValue = "")String productCate){
        List<RevenuParCategorie> parCategories=new ArrayList<>();
        for (Object[] o:commandeRepository.getRevenuMensuel()){
            parCategories.add(RevenuParCategorie.builder()
                    .revenu((Double) o[1])
                    .date((Date) o[0])
                    .build());
        }
        return parCategories;
    }
    @GetMapping("/orders")
    public List<Commande> getOrders(){
        return serviceMetier.getAllCommandes();
    }
    @GetMapping("/orders/non_delivered")
    public HashMap<String,Integer> getNonDeliveredOrders(){
        HashMap<String,Integer> response=new HashMap<>();
        response.put("number",commandeRepository.getNonDelivered().size());
        return response;
    }
}
