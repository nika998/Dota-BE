package com.artigo.dota.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailsSubmitDTO {

    private Long id;

    @NotNull(message = "Color is required")
    private String color;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 0, message = "Quantity must be non-negative")
    private int quantity;

    private String info;

    @Valid
    @NotNull(message = "Product images are required")
    private List<ProductImageDTO> images;
}
