package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductDO, UUID> {

    List<ProductDO> findByType(String type);

    Page<ProductDO> findByType(String type, Pageable pageable);
}
