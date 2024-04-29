package com.almou.metier;

import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;

@Projection(types = {Commande.class},name = "proj1")
public interface CommandeProjection {
    AppUser getUser();
    List<ProductQuantity> getArticles();
    Date getDate_commande();
    Boolean getShipped();
    Boolean getPayed();
    Long getId();
}
