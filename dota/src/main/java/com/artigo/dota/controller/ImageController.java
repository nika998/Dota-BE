package com.artigo.dota.controller;

import com.artigo.dota.service.ProductImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("/images")
public class ImageController {

    private final ProductImageService productImageService;

    public ImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping(value = "/upload-image",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(@RequestParam("file") MultipartFile file,
                            @RequestParam("imageUrl") String imageUrl) {
        productImageService.uploadProductImage(imageUrl, file);
    }

    @GetMapping(value = "/{productImageId}/get-image",
                produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("productImageId") UUID productImageId) {
        return productImageService.getProductImage(productImageId);
    }
}
