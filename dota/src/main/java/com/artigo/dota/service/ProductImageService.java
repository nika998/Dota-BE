package com.artigo.dota.service;

import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductImageDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {
    String uploadProductImage(String imageUrl, MultipartFile file);

    byte[] getProductImage(UUID productImageId);

    List<ProductImageUrlDTO> uploadProductImages(List<ProductImageDTO> images, String type, String name, String color);

    List<ProductImageDO> saveAll(List<ProductImageDO> images);

    boolean deleteImage(String key);

    void deleteUploadedImages(List<ProductImageUrlDTO> uploadedImagesDTO);

    List<ProductImageDO> deleteProductImagesList(List<ProductImageDO> images);
}
