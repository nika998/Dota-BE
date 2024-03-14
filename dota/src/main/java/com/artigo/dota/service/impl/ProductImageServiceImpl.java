package com.artigo.dota.service.impl;

import com.artigo.dota.configuration.S3BucketProperties;
import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.dto.converter.ProductImageToProductImageUrl;
import com.artigo.dota.entity.ProductImageDO;
import com.artigo.dota.repository.ProductImageRepository;
import com.artigo.dota.service.ProductImageService;
import com.artigo.dota.service.impl.s3.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final S3Service s3Service;
    private final ProductImageRepository productImageRepository;
    private final S3BucketProperties s3Bucket;
    private final ProductImageToProductImageUrl productImageConverter;

    public ProductImageServiceImpl(S3Service s3Service, ProductImageRepository productImageRepository, S3BucketProperties s3Bucket, ProductImageToProductImageUrl productImageConverter) {
        this.s3Service = s3Service;
        this.productImageRepository = productImageRepository;
        this.s3Bucket = s3Bucket;
        this.productImageConverter = productImageConverter;
    }

    @Override
    public String uploadProductImage(String imageUrl, MultipartFile file) {
        String contentType = file.getContentType();
        String extension = contentType.equals("image/jpeg") ? "jpg" : "png";

        // Generate a unique key for the S3 object
        String key = imageUrl + "." + extension;

        try {
            s3Service.putImageFile(
                    s3Bucket.getBucket(),
                    key,
                    file.getBytes()
            );

            return key;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteObject(String key) {
        s3Service.deleteImage(
                s3Bucket.getBucket(),
                key
        );
    }

    @Override
    public void deleteUploadedImages(List<ProductImageUrlDTO> uploadedImagesDTO) {
        for(ProductImageUrlDTO productImageUrlDTO : uploadedImagesDTO) {
            String imageS3Url = productImageUrlDTO.getImagePath();
            String key = imageS3Url.substring(s3Bucket.getImageUrlPrefix().length() + 1);
            this.deleteObject(key);
        }
    }

    @Override
    public byte[] getProductImage(Long productImageId) {
//        ProductImageDO foundProductImageDO = productImageRepository.getReferenceById(productImageId);
//        if(foundProductImageDO == null){
//            log.error("Product image with id " + productImageId + "not found");
//            return null;
//        }
        return s3Service.getObject(
                s3Bucket.getBucket(),
                "images/torbica/republika-sumska/crna"
//                foundProductImageDO.getImagePath()
        );
    }

    @Override
    public List<ProductImageUrlDTO> uploadProductImages(List<ProductImageDTO> images, String type, String name) {
        List<ProductImageUrlDTO> uploadedProductImagesDTO = new ArrayList<>();
        if(images != null) {
            for (ProductImageDTO productImageDTO : images) {
                String imageUrl =
                        s3Bucket.getRootFolder() + "/" + type + "/" + name + "/" + productImageDTO.getColor() + "/" + UUID.randomUUID();
                String uploadedImageUrl = this.uploadProductImage(imageUrl, productImageDTO.getFile());
                if(uploadedImageUrl != null) {
                    ProductImageUrlDTO convertedProductImageUrlDTO =
                            productImageConverter.convert(productImageDTO, s3Bucket.getImageUrlPrefix() + "/" + uploadedImageUrl);
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
