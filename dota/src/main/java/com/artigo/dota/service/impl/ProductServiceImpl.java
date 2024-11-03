package com.artigo.dota.service.impl;

import com.artigo.dota.dto.*;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductDetailsDO;
import com.artigo.dota.exception.EntityNotFoundException;
import com.artigo.dota.exception.ImageProcessingException;
import com.artigo.dota.exception.ProductNotProcessedException;
import com.artigo.dota.mapper.ProductDetailsMapper;
import com.artigo.dota.mapper.ProductImageMapper;
import com.artigo.dota.mapper.ProductMapper;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.ProductDetailsService;
import com.artigo.dota.service.ProductImageService;
import com.artigo.dota.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "products", key = "#pageable.pageNumber")
    public Page<ProductDTO> getProductsPageable(Pageable pageable) {
        Page<ProductDO> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::entityToDto);
    }

    @Override
    public List<ProductDTO> getProductsByType(String type) {
        List<ProductDO> productsByType = productRepository.findByType(type);
        return productsByType.stream().map(productMapper::entityToDto).toList();
    }

    @Override
    public Page<ProductDTO> getProductsByTypePageable(Pageable pageable, String type) {
        Page<ProductDO> productsByType = productRepository.findByType(type, pageable);
        return productsByType.map(productMapper::entityToDto);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductDTO getProductById(Long id) {
        ProductDO product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with provided id not found"));
        return productMapper.entityToDto(product);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductDTO processProduct(ProductSubmitDTO product, List<MultipartFile> files) throws ImageProcessingException, ProductNotProcessedException {
        var productDetailsSubmitDTOs = product.getProductDetails();
        product.setProductDetails(null);
        ProductDO savedProduct = productRepository.save(productMapper.dtoToEntity(product));
        savedProduct.setProductDetails(new ArrayList<>());
        for (ProductDetailsSubmitDTO productDetailsSubmitDTO:
             productDetailsSubmitDTOs) {
            productDetailsSubmitDTO.setProductId(savedProduct.getId());
            savedProduct.getProductDetails().add(processProductDetails(product, productDetailsSubmitDTO, files));
        }
        return productMapper.entityToDto(savedProduct);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductDTO deleteProduct(Long id) {
        ProductDO product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with provided id not found"));
        productDetailsService.deleteProductDetailsList(product.getProductDetails());
        product.setIsDeleted(Boolean.TRUE);
        ProductDO deletedProduct = productRepository.save(product);

        ProductDTO deletedProductDTO = productMapper.entityToDto(deletedProduct);
        List<ProductImageUrlDTO> imagesToDelete = new ArrayList<>();
        deletedProductDTO.getProductDetails().forEach(productDetailsDTO ->
            imagesToDelete.addAll(productDetailsDTO.getImages())
        );
        productImageService.deleteUploadedImages(imagesToDelete);

        return deletedProductDTO;
    }

    @Override
    @Transactional
    public ProductDetailsDO processProductDetails(ProductSubmitDTO product, ProductDetailsSubmitDTO productDetails, List<MultipartFile> files) throws ImageProcessingException, ProductNotProcessedException {
        if(files.size() < productDetails.getImages().size()) {
            throw new ImageProcessingException("Image files do not match with ProductImageDTOs");
        }

        for (MultipartFile file : files) {
            // Get the productId associated with the file
            String originalFilename = file.getOriginalFilename();
            if(originalFilename != null && originalFilename.contains(".")) {
                String[] parts = originalFilename.split("\\.");
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
            throw new ProductNotProcessedException("Failed to save Product");
        }
    }
}
