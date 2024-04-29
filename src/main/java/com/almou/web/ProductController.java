package com.almou.web;

import com.almou.metier.Article;
import com.almou.metier.ServiceMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
	@Autowired
	private ServiceMetier serviceMetier;
	@GetMapping(value = "/produits/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Article getProductById(@PathVariable("id") Long id){
		return serviceMetier.getProductById(id).get();
	}
}