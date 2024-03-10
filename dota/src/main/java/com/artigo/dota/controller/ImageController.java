package com.artigo.dota.controller;

import com.artigo.dota.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,
                              @RequestParam("directoryPath") String directoryPath,
                              @RequestParam("fileName") String fileName) {
        return imageService.saveImage(file, directoryPath, fileName);
    }

    @PostMapping("/get")
    public ResponseEntity<byte[]> getImage(@RequestParam("directoryPath") String directoryPath,
                                           @RequestParam("fileName") String fileName) {
        // Load the image as a classpath resource
        byte[] imageData = imageService.getImage(directoryPath, fileName);
        if (imageData != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
