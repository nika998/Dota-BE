package com.artigo.dota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductImageUrlDTO {

    private Long id;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "isDisplay is required")
    private Boolean isDisplay;

    @NotBlank(message = "Image path is required")
    private String imagePath;
}
