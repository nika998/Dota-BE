package com.artigo.dota.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductImageDTO {

    private Long id;
    private String color;
    private Boolean isDisplay;
    private Long productImageId;
    private MultipartFile file;
}
