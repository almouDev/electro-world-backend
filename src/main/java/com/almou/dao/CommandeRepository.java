package com.almou.dao;

import com.almou.metier.Commande;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommandeRepository extends CrudRepository<Commande,Long> {
    @Query("select date(pd.date_commande) as date, SUM(pd.total) as revenu from Commande pd  where " +
            "month (pd.date_commande)=(month (current_timestamp)-1) group by day(pd.date_commande) order by date(pd.date_commande)")
    List<Object[]> getRevenuMensuel();

    @Query("from Commande cmd where cmd.shipped=false ")
    List<Commande> getNonDelivered();
}
