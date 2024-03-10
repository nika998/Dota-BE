package com.artigo.dota.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String saveImage(MultipartFile image, String imageDirectoryPath, String imageName);

    byte[] getImage(String directoryPath, String fileName);
}
