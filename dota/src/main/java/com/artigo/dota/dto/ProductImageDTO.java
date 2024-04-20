package com.artigo.dota.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductImageDTO {

    private Long id;

    @NotNull(message = "isDisplay is required")
    private Boolean isDisplay;

    @NotNull(message = "Product image ID is required")
    private Long productImageId;

    private MultipartFile file;
}
