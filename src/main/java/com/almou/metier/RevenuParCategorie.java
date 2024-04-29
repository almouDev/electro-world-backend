package com.almou.metier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class RevenuParCategorie {
    private String categorie;
    private double revenu;
    private Date date;
}
