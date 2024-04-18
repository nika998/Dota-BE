package com.artigo.dota.controller;

import com.artigo.dota.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping(value = "/upload-image",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(@RequestParam("file") MultipartFile file,
                            @RequestParam("imageUrl") String imageUrl) {
        productImageService.uploadProductImage(imageUrl, file);
    }

    @GetMapping(value = "/{productImageId}/get-image",
                produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("productImageId") Long productImageId) {
        return productImageService.getProductImage(productImageId);
    }
}
