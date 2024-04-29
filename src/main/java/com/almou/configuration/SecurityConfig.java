package com.almou.configuration;
import com.almou.metier.AppUser;
import com.almou.metier.Article;
import com.almou.metier.Commande;
import com.almou.metier.ServiceMetier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final ServiceMetier serviceMetier;
	private  final AuthenticationManager authenticationManager;

	public SecurityConfig(ServiceMetier serviceMetier,@Lazy AuthenticationManager authenticationManager) {
		this.authenticationManager=authenticationManager;
		this.serviceMetier = serviceMetier;
	}

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return
				http
				.authorizeHttpRequests(requests->requests.requestMatchers("/produits/**","/refreshToken","/signup","/products/images/**").permitAll()
						.requestMatchers(HttpMethod.GET,"/articles/**","/categories/**").permitAll()
						.requestMatchers(HttpMethod.POST,"/articles/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.PUT,"/articles/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.PATCH,"/articles/**").hasAuthority("ADMIN")
						.requestMatchers("/commandes").hasAuthority("ADMIN")
						.anyRequest().authenticated()
				)
				.addFilter(new JwtAuthenticationFilter(authenticationManager,serviceMetier))
				.addFilterBefore(new JwtAuthorizationToken(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
						.csrf().disable()
						.cors().configurationSource(corsConfigurationSource()).and()
				.build();
    }
	@Bean
	public UserDetailsService userDetails() {
		return username -> {
			AppUser appUser=serviceMetier.loadByEmail(username);
			Collection<GrantedAuthority> authorities=new ArrayList<>();
			appUser.getUserRoles().forEach(appRole -> {
				authorities.add(new SimpleGrantedAuthority(appRole.getName()));
			});
			return new  User(username,appUser.getPassword(), authorities);
		};
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource(){
		CorsConfiguration configuration=new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PATCH","PUT"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",configuration);
		return source;
	}
	@Bean
	RepositoryRestConfigurer restConfigurer(){
		return new RepositoryRestConfigurer() {
			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
				config.exposeIdsFor(Article.class);
				config.exposeIdsFor(Commande.class);
				config.exposeIdsFor(AppUser.class);
			}
		};
	}
}