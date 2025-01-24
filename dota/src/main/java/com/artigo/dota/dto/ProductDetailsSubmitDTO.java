package com.artigo.dota.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductDetailsSubmitDTO {

    private UUID id;

    @NotNull(message = "Color is required")
    private String color;

    private UUID productId;

    @Min(value = 0, message = "Quantity must be non-negative")
    private int quantity;

    private String info;

    @Valid
    @NotNull(message = "Product images are required")
    private List<ProductImageDTO> images;
}
