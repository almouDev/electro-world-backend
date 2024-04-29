package com.almou.configuration;

import com.almou.metier.AppUser;
import com.almou.metier.ServiceMetier;
import com.almou.utilities.JwtUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private ServiceMetier serviceMetier;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ServiceMetier serviceMetier) {
        this.authenticationManager = authenticationManager;
        this.serviceMetier = serviceMetier;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String pw=request.getParameter("password");
        String username=request.getParameter("username");
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(username,pw);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user= (User) authResult.getPrincipal();
        AppUser appUser= serviceMetier.loadByEmail(user.getUsername());
        Collection<String> authorities=user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList());
        String jwtAccessToken= JwtUtilities
                .JwtTokenCreation(request.getRequestURL().toString(), 15*60*1000,authorities,user.getUsername())
                .withClaim("nom",appUser.getNom())
                .withClaim("prenom",appUser.getPrenom())
                .sign(JwtUtilities.getAlgorithm());
        String jwtRefreshToken=JwtUtilities.JwtTokenCreation(request.getRequestURL().toString(), 5*60*60*1000,authorities,user.getUsername())
                .sign(JwtUtilities.getAlgorithm());
        JwtUtilities.SendTokens(jwtAccessToken,jwtRefreshToken,response);
    }
}