package com.almou.metier;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductImage implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private  String image;
}
