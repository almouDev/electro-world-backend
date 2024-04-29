package com.almou.web;
import com.almou.metier.AppUser;
import com.almou.metier.ServiceMetier;
import com.almou.utilities.MainUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private ServiceMetier serviceMetier;
    //La liste de tous les utilisateurs
    @GetMapping(path="/utilisateurs")
    public List<AppUser> getUsers(){
        return serviceMetier.findAllUsers();
    }
    //L'inscription
    @PostMapping(path = "/signup")
    public AppUser signup(@RequestBody AppUser user, HttpServletResponse response) throws IOException {
        AppUser user1=serviceMetier.loadByEmail(user.getEmail());
        if (user1!=null){
            MainUtils.sendError("L'utilisateur existe déja", HttpStatus.INTERNAL_SERVER_ERROR.value(), response);
            return null;
        }
        else {
        serviceMetier.saveUser(user);
        return user;
        }
    }
    @DeleteMapping(path = "/utilisateurs/{id}")
    public void deleteUser(@PathVariable("id")Long id){
        serviceMetier.deleteUser(id);
    }
}
