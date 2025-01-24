package com.artigo.dota.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class ProductImageDTO {

    private UUID id;

    @NotNull(message = "isDisplay is required")
    private Boolean isDisplay;

    @NotNull(message = "Product image ID is required")
    private Long productImageId;

    private MultipartFile file;
}
