package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductDetailsDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailsRepository extends JpaRepository<ProductDetailsDO, Long> {
    Optional<ProductDetailsDO> findByIdAndIsDeletedFalse(Long id);
}
