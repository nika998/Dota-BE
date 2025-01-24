package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductImageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageDO, UUID> {
    Optional<ProductImageDO> findByIdAndIsDeletedFalse(UUID productImageId);
}
