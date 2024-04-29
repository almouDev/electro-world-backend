package com.almou.metier;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Entity @AllArgsConstructor @Data @NoArgsConstructor
public class Commande implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="command_id")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_commande;
	private Boolean shipped;
	@ManyToOne
	private AppUser user;
	private Boolean payed;
	private double total;
	@OneToMany(mappedBy = "commande",cascade = CascadeType.ALL)
	private Collection<ProductQuantity> articles=new ArrayList<>();
}
