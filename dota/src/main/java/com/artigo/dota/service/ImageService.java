package com.artigo.dota.service;

public interface ImageService {

    String getRootFolder();

    String getImageUrlPrefix();

    String saveImage(String key, byte[] file);

    boolean deleteImage(String key);

    byte[] getImage(String imageUrl);
}
