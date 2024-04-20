package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductDetailsDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailsRepository extends JpaRepository<ProductDetailsDO, Long> {
}
