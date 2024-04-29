package com.almou.dao;

import com.almou.metier.ProductQuantity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ProductQuantityRepository extends CrudRepository<ProductQuantity,Long> {
    @Query("select pd.article.categorie.categorieName as categorie, SUM(pd.commande.total) as revenu from ProductQuantity pd where " +
            "YEAR (pd.commande.date_commande)=year (current_timestamp) group by pd.article.categorie.categorieName")
    List<Object[]> getRevenuAnnuel();
}
