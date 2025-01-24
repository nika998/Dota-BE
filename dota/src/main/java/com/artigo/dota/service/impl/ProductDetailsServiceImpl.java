package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ProductDetailQuantityDTO;
import com.artigo.dota.dto.ProductDetailsDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductDetailsDO;
import com.artigo.dota.entity.ProductImageDO;
import com.artigo.dota.exception.EntityNotFoundException;
import com.artigo.dota.mapper.ProductDetailQuantityMapper;
import com.artigo.dota.mapper.ProductDetailsMapper;
import com.artigo.dota.mapper.ProductImageMapper;
import com.artigo.dota.repository.ProductDetailsRepository;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.ProductDetailsService;
import com.artigo.dota.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;

    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    private final ProductDetailsMapper productDetailsMapper;

    private final ProductImageMapper productImageMapper;

    private final ProductDetailQuantityMapper productDetailQuantityMapper;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository, ProductRepository productRepository, ProductImageService productImageService, ProductDetailsMapper productDetailsMapper, ProductImageMapper productImageMapper, ProductDetailQuantityMapper productDetailQuantityMapper) {
        this.productDetailsRepository = productDetailsRepository;
        this.productRepository = productRepository;
        this.productImageService = productImageService;
        this.productDetailsMapper = productDetailsMapper;
        this.productImageMapper = productImageMapper;
        this.productDetailQuantityMapper = productDetailQuantityMapper;
    }

    @Override
    public ProductDetailsDTO getProductById(UUID id) {
        var productDetailsDO = productDetailsRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EntityNotFoundException("Product details with provided id not found"));
        return productDetailsMapper.entityToDto(productDetailsDO);
    }

    @Override
    public List<ProductDetailQuantityDTO> getProductDetailQuantities(List<UUID> idList) {
        return idList.stream()
                .map(id -> productDetailsRepository.findByIdAndIsDeletedFalse(id).orElse(null))
                .filter(Objects::nonNull)
                .map(productDetailQuantityMapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public List<ProductDetailsDO> deleteProductDetailsList(List<ProductDetailsDO> productDetails) {
        List<ProductDetailsDO> deletedProductDetails = new ArrayList<>();
        productDetails.forEach(productDetailsDO -> {
            productImageService.deleteProductImagesList(productDetailsDO.getImages());
            productDetailsDO.setIsDeleted(Boolean.TRUE);
            deletedProductDetails.add(productDetailsDO);
        });
        return deletedProductDetails;
    }

    @Override
    @Transactional
    public ProductDetailsDTO deleteProductDetail(UUID id) {
        ProductDetailsDO productDetails = productDetailsRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new EntityNotFoundException("Product details with provided id not found"));
        productImageService.deleteProductImagesList(productDetails.getImages());
        productDetails.setIsDeleted(Boolean.TRUE);
        ProductDetailsDO deletedProductDetailsDO = productDetailsRepository.save(productDetails);

        ProductDetailsDTO deletedProductDetailDTO = productDetailsMapper.entityToDto(deletedProductDetailsDO);
        List<ProductImageUrlDTO> imagesToDelete = deletedProductDetailDTO.getImages();
        productImageService.deleteUploadedImages(imagesToDelete);

        return deletedProductDetailDTO;
    }

    @Override
    @Transactional
    public boolean reduceProductQuantity(UUID productDetailsId, int orderedQuantity) {
        var productDetailsDO = productDetailsRepository.findByIdAndIsDeletedFalse(productDetailsId);
        if(productDetailsDO.isPresent() && productDetailsDO.get().getQuantity() >= orderedQuantity) {
            productDetailsDO.get().setQuantity(productDetailsDO.get().getQuantity() - orderedQuantity);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkProductAvailability(UUID productDetailsId, int orderedQuantity) {
        var productDetailsDO = productDetailsRepository.findByIdAndIsDeletedFalse(productDetailsId);
        return (productDetailsDO.isPresent() && productDetailsDO.get().getQuantity() >= orderedQuantity);
    }

    @Override
    public ProductDO getRelatedProduct(ProductDetailsDO productDetailsDO) {
        return productRepository.findById(productDetailsDO.getProductId()).orElse(null);
    }

    @Override
    @Transactional
    public List<ProductDetailsDO> saveAll(List<ProductDetailsDO> productDetailsDOList) {
        return productDetailsRepository.saveAll(productDetailsDOList);
    }

    @Override
    @Transactional
    public ProductDetailsDO saveProductDetails(ProductDetailsDTO productDetailsDTO, List<ProductImageUrlDTO> uploadedImagesDTO) {
        ProductDetailsDO productDetailsDO = productDetailsMapper.dtoToEntity(productDetailsDTO);

        List<ProductImageDO> uploadImagesDO = uploadedImagesDTO.stream().map(productImageMapper::dtoToEntity)
                .toList();

        ProductDetailsDO savedProductDetails = productDetailsRepository.save(productDetailsDO);

        for (ProductImageDO productImageDO:
                uploadImagesDO) {
            productImageDO.setProductDetailId(savedProductDetails.getId());
        }
        List<ProductImageDO> savedProductImagesDO = productImageService.saveAll(uploadImagesDO);
        savedProductDetails.setImages(savedProductImagesDO);
        return savedProductDetails;
    }
}
