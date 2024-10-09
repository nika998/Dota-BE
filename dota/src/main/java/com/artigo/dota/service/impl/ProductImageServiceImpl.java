package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductImageDO;
import com.artigo.dota.mapper.ProductImageMapper;
import com.artigo.dota.repository.ProductImageRepository;
import com.artigo.dota.service.ImageService;
import com.artigo.dota.service.ProductImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    private final ImageService imageService;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ProductImageMapper productImageMapper, ImageService imageService) {
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
        this.imageService = imageService;
    }

    @Override
    public String uploadProductImage(String imageUrl, MultipartFile file) {
        String contentType = file.getContentType();
        if(contentType == null) {
            return null;
        }
        String extension = contentType.equals("image/jpeg") ? "jpg" : "png";

        // Generate a unique key for the S3 object
        String key = imageUrl + "." + extension;

        try {
            return imageService.saveImage(key, file.getBytes());
        } catch (IOException e) {
            log.error("Cannot read image into byte array");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteImage(String key) {
        return imageService.deleteImage(key);
    }

    @Override
    public void deleteUploadedImages(List<ProductImageUrlDTO> uploadedImagesDTO) {
        for(ProductImageUrlDTO productImageUrlDTO : uploadedImagesDTO) {
            String imageS3Url = productImageUrlDTO.getImagePath();
            String key = imageS3Url.substring(imageService.getImageUrlPrefix().length() + 1);
            this.deleteImage(key);
        }
    }

    @Override
    @Transactional
    public List<ProductImageDO> deleteProductImagesList(List<ProductImageDO> images) {
        List<ProductImageDO> deletedProductImages = new ArrayList<>();
        images.forEach(productImageDO -> {
            productImageDO.setIsDeleted(Boolean.TRUE);
            deletedProductImages.add(productImageDO);
        });
        return deletedProductImages;
    }

    @Override
    public byte[] getProductImage(Long productImageId) {
        Optional<ProductImageDO> foundProductImageDO = productImageRepository.findByIdAndIsDeletedFalse(productImageId);
        if(foundProductImageDO.isEmpty()){
            log.error("Product image with id " + productImageId + "not found");
            return new byte[0];
        } else {
            return imageService.getImage(foundProductImageDO.get().getImagePath());
        }
    }

    @Override
    public List<ProductImageUrlDTO> uploadProductImages(List<ProductImageDTO> images, String type, String name, String color) {
        List<ProductImageUrlDTO> uploadedProductImagesDTO = new ArrayList<>();
        if(images != null) {
            for (ProductImageDTO productImageDTO : images) {
                String namePathValue = name.replace(" ", "_");
                String colorPathValue = color.substring(1);
                String imageUrl =
                        imageService.getRootFolder() + "/" + type + "/" + namePathValue + "/"  + colorPathValue + "/" + UUID.randomUUID();
                String uploadedImageUrl = this.uploadProductImage(imageUrl, productImageDTO.getFile());
                if(uploadedImageUrl != null) {
                    ProductImageUrlDTO convertedProductImageUrlDTO =
                            productImageMapper.dtoToUrlDto(productImageDTO, imageService.getImageUrlPrefix() + "/" + uploadedImageUrl);
                    uploadedProductImagesDTO.add(convertedProductImageUrlDTO);
                } else {
                    return uploadedProductImagesDTO;
                }
            }

        }
        return uploadedProductImagesDTO;
    }

    @Override
    @Transactional
    public List<ProductImageDO> saveAll(List<ProductImageDO> images) {
        return productImageRepository.saveAll(images);
    }

}
