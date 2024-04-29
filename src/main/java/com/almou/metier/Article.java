package com.almou.metier;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity(name="produits")
@Builder
public class Article implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ref_article")
	private Long id;
	private double Oprix;
	private String designation;
	private double prix;
	private String caracteristiques;
	private int stock;
	@OneToMany(cascade = CascadeType.REMOVE,mappedBy = "article")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<ProductQuantity> productQuantities;
	@OneToMany
	private List<ProductImage> image=new ArrayList<>();
	@ManyToOne
	private Categorie categorie;
}
