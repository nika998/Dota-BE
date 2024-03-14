package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductImageDO;
import com.artigo.dota.mapper.ProductImageMapper;
import com.artigo.dota.mapper.ProductMapper;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.ProductImageService;
import com.artigo.dota.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductImageMapper productImageMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        return  productRepository.findAll().stream().map(productMapper::entityToDto)
                .toList();

    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductDO product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.entityToDto(product);
    }

    @Override
    @Transactional
    public ProductDO saveProduct(ProductDTO product, List<ProductImageUrlDTO> uploadedImagesDTO) {
        ProductDO productDO = productMapper.dtoToEntity(product);
        List<ProductImageDO> uploadImagesDO = uploadedImagesDTO.stream().map(productImageMapper::dtoToEntity)
                .toList();
        ProductDO savedProduct = productRepository.save(productDO);
        for (ProductImageDO productImageDO:
                uploadImagesDO) {
             productImageDO.setProductId(savedProduct.getId());
        }
        List<ProductImageDO> savedProductImagesDO = productImageService.saveAll(uploadImagesDO);
        savedProduct.setImages(savedProductImagesDO);
        return savedProduct;
    }
}
