package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductDetailsDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductDetailsRepository extends JpaRepository<ProductDetailsDO, UUID> {
    Optional<ProductDetailsDO> findByIdAndIsDeletedFalse(UUID id);
}
