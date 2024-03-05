package com.artigo.dota.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Configuration
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void storeFile(MultipartFile file, Long productId) {
        File directory = new File(uploadDir + "/" + productId);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            file.transferTo(new File(directory.getAbsolutePath() + "/" + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}