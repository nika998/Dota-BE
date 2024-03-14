package com.artigo.dota.service;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductDO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDO saveProduct(ProductDTO product, List<ProductImageUrlDTO> uploadedImagesDTO);
}
