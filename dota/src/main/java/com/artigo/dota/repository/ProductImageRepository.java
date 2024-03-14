package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductImageDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageDO, Long> {
}
