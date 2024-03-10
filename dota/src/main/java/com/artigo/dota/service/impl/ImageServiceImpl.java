package com.artigo.dota.service.impl;

import com.artigo.dota.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${file.upload-dir}") // Define the directory where files will be saved in application.properties
    private String imageLocation;

    public String saveImage(MultipartFile image, String imageDirectoryPath, String imageName) {
        // Create the directory if it doesn't exist
        File directory = new File(imageLocation + "/" + imageDirectoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to the specified directory
        Path filePath = Paths.get(imageLocation + "/" + imageDirectoryPath, imageName);
        try {
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }

        // Return the path where the file was saved
        return filePath.toString();
    }


    @Override
    public byte[] getImage(String imageDirectoryPath, String imageName) {
        Resource resource = new FileSystemResource(imageLocation + "/" + imageDirectoryPath + "/" + imageName);

        if (resource.exists() && resource.isReadable()) {
            try (InputStream inputStream = resource.getInputStream()) {
                return inputStream.readAllBytes();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }
}
