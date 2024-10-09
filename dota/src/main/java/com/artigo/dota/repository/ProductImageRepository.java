package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductImageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageDO, Long> {
    Optional<ProductImageDO> findByIdAndIsDeletedFalse(Long productImageId);
}
