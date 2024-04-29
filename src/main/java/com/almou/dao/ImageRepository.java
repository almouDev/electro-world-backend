package com.almou.dao;

import com.almou.metier.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ProductImage,Long>{
}
