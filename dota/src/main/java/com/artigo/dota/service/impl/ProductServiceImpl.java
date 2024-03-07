package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.mapper.ProductMapper;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EmailService emailService;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<String> recipientsList = new ArrayList<>(Arrays.asList("dulovicnika27@gmail.com"));
        Map<String,String> orderInfo = Map.of("title","New order has arrived");

        emailService.sendOrderMail(recipientsList, orderInfo);
        return  productRepository.findAll().stream().map(productMapper::entityToDto)
                .collect(Collectors.toList());

    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductDO product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.entityToDto(product);
    }
}
