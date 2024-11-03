package com.artigo.dota.repository;

import com.artigo.dota.entity.ProductDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductDO, Long> {

    List<ProductDO> findByType(String type);

    Page<ProductDO> findByType(String type, Pageable pageable);
}
