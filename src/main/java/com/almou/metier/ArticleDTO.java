package com.almou.metier;

import lombok.Builder;

import java.util.List;
@Builder
public record ArticleDTO(String designation,Double prix,List<ProductImage> images,Long id) {
}
