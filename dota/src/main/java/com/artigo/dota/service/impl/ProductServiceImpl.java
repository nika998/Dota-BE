package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.mapper.ProductMapper;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        return  productRepository.findAll().stream().map(productMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductDO product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.entityToDto(product);
    }
}
