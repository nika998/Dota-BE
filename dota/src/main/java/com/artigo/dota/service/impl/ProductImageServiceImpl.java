package com.artigo.dota.service.impl;

import com.artigo.dota.configuration.S3BucketProperties;
import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductImageDO;
import com.artigo.dota.mapper.ProductImageMapper;
import com.artigo.dota.repository.ProductImageRepository;
import com.artigo.dota.service.ProductImageService;
import com.artigo.dota.service.impl.s3.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    private final S3Service s3Service;

    private final S3BucketProperties s3Bucket;

    public ProductImageServiceImpl(S3Service s3Service, ProductImageRepository productImageRepository, S3BucketProperties s3Bucket, ProductImageMapper productImageMapper) {
        this.s3Service = s3Service;
        this.productImageRepository = productImageRepository;
        this.s3Bucket = s3Bucket;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public String uploadProductImage(String imageUrl, MultipartFile file) {
        String contentType = file.getContentType();
        String extension = contentType.equals("image/jpeg") ? "jpg" : "png";

        // Generate a unique key for the S3 object
        String key = imageUrl + "." + extension;

        try {
            s3Service.saveImageFile(
                    s3Bucket.getBucket(),
                    key,
                    file.getBytes()
            );

            return key;
        } catch (SdkClientException e) {
            log.error("Cannot save image inside s3 bucket");
            e.printStackTrace();

        } catch (IOException e) {
            log.error("Cannot read image into byte array");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteImage(String key) {
        return s3Service.deleteImage(
                s3Bucket.getBucket(),
                key
        );
    }

    @Override
    public void deleteUploadedImages(List<ProductImageUrlDTO> uploadedImagesDTO) {
        for(ProductImageUrlDTO productImageUrlDTO : uploadedImagesDTO) {
            String imageS3Url = productImageUrlDTO.getImagePath();
            String key = imageS3Url.substring(s3Bucket.getImageUrlPrefix().length() + 1);
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
//        ProductImageDO foundProductImageDO = productImageRepository.getReferenceById(productImageId);
//        if(foundProductImageDO == null){
//            log.error("Product image with id " + productImageId + "not found");
//            return null;
//        }
        return s3Service.getImage(
                s3Bucket.getBucket(),
                "images/torbica/republika-sumska/crna"
//                foundProductImageDO.getImagePath()
        );
    }

    @Override
    public List<ProductImageUrlDTO> uploadProductImages(List<ProductImageDTO> images, String type, String name, String color) {
        List<ProductImageUrlDTO> uploadedProductImagesDTO = new ArrayList<>();
        if(images != null) {
            for (ProductImageDTO productImageDTO : images) {
                name = name.replace(" ", "_");
                color = color.substring(1);
                String imageUrl =
                        s3Bucket.getRootFolder() + "/" + type + "/" + name + "/"  + color + "/" + UUID.randomUUID();
                String uploadedImageUrl = this.uploadProductImage(imageUrl, productImageDTO.getFile());
                if(uploadedImageUrl != null) {
                    ProductImageUrlDTO convertedProductImageUrlDTO =
                            productImageMapper.dtoToUrlDto(productImageDTO, s3Bucket.getImageUrlPrefix() + "/" + uploadedImageUrl);
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
