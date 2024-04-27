package com.artigo.dota.service.impl;

import com.artigo.dota.dto.*;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductDetailsDO;
import com.artigo.dota.exception.ImageProcessingException;
import com.artigo.dota.mapper.ProductDetailsMapper;
import com.artigo.dota.mapper.ProductImageMapper;
import com.artigo.dota.mapper.ProductMapper;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.ProductDetailsService;
import com.artigo.dota.service.ProductImageService;
import com.artigo.dota.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductDetailsService productDetailsService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Autowired
    private ProductImageMapper productImageMapper;


    @Override
    public List<ProductDTO> getAllProducts() {
        return  productRepository.findAll().stream().map(productMapper::entityToDto)
                .toList();
    }

    @Override
    public Page<ProductDTO> getProductsByPage(Pageable pageable) {
        Page<ProductDO> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::entityToDto);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        ProductDO product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.entityToDto(product);
    }

    @Override
    @Transactional
    public ProductDTO processProduct(ProductSubmitDTO product, List<MultipartFile> files) {
        var productDetailsSubmitDTOs = product.getProductDetails();
        product.setProductDetails(null);
        ProductDO savedProduct = productRepository.save(productMapper.dtoToEntity(product));
        savedProduct.setProductDetails(new ArrayList<>());
        productDetailsSubmitDTOs
                .forEach(productDetailsSubmitDTO -> {
                    productDetailsSubmitDTO.setProductId(savedProduct.getId());
                    savedProduct.getProductDetails().add(processProductDetails(product, productDetailsSubmitDTO, files));
                });
        return productMapper.entityToDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDetailsDO processProductDetails(ProductSubmitDTO product, ProductDetailsSubmitDTO productDetails, List<MultipartFile> files) {
        if(files.size() < productDetails.getImages().size()) {
            throw new ImageProcessingException("Image files do not match with ProductImageDTOs");
        }

        for (MultipartFile file : files) {
            // Get the productId associated with the file
            if(file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")) {
                String[] parts = file.getOriginalFilename().split("\\.");
                Long productId = Long.parseLong(parts[0]);

                // Find the corresponding ProductImageDTO using the productId
                ProductImageDTO productImageDTO = productDetails.getImages()
                        .stream()
                        .filter(image -> productId.equals(image.getProductImageId()))
                        .findFirst()
                        .orElse(null);

                if (productImageDTO != null) {
                    // Associate the file with the corresponding ProductImageDTO
                    productImageDTO.setFile(file);
                }
            } else {
                throw new ImageProcessingException("Image files are not named properly");
            }

        }

        List<ProductImageUrlDTO> uploadedImagesDTO =
                productImageService.uploadProductImages(productDetails.getImages(), product.getType(), product.getName(), productDetails.getColor());

        if(uploadedImagesDTO.size() != productDetails.getImages().size()) {
            productImageService.deleteUploadedImages(uploadedImagesDTO);
            throw new ImageProcessingException("Failed to upload product images");
        }

        try {
            ProductDetailsDTO productDetailsDTO = productDetailsMapper.submitDtoToDto(productDetails);
            return productDetailsService.saveProductDetails(productDetailsDTO, uploadedImagesDTO);
        } catch (RuntimeException e) {
            productImageService.deleteUploadedImages(uploadedImagesDTO);
            throw new RuntimeException("Failed to save Product");
        }
    }
}
