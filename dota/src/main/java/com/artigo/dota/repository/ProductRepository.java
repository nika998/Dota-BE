package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDO, Long> {
}
