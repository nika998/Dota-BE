package com.artigo.dota.service;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getProductsByPage(Pageable pageable);

    ProductDTO getProductById(Long id);
    ProductDTO saveProduct(ProductDTO product, List<ProductImageUrlDTO> uploadedImagesDTO);
}
