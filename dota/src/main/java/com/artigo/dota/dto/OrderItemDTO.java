package com.artigo.dota.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDTO {

    private Long id;

    @NotNull(message = "Order ID is required")
    private Long order;

    @NotNull(message = "Product is required")
    @Valid
    private ProductDTO product;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Color is required")
    private String color;
}
