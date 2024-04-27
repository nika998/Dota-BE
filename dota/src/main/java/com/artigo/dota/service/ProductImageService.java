package com.artigo.dota.service;

import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductImageDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    String uploadProductImage(String imageUrl, MultipartFile file);

    byte[] getProductImage(Long productImageId);

    List<ProductImageUrlDTO> uploadProductImages(List<ProductImageDTO> images, String type, String name, String color);

    List<ProductImageDO> saveAll(List<ProductImageDO> images);

    boolean deleteImage(String key);

    void deleteUploadedImages(List<ProductImageUrlDTO> uploadedImagesDTO);
}
