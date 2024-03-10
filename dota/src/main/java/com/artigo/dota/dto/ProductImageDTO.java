package com.artigo.dota.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

    private Long id;
    private String color;
    private boolean isDisplay;
    private MultipartFile multipartFile;
}
