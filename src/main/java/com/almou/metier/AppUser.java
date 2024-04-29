package com.almou.metier;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
@Entity(name="utilisateurs")
@Data @NoArgsConstructor @AllArgsConstructor
public class AppUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long user_id;
	@Column(unique = true)
	private String email;
	private String nom;
	private String prenom;
	private Boolean vente_flash;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String  password;
	private String adress;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable()
	private Collection<AppRole> userRoles=new ArrayList<>();
}