package com.artigo.dota.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemDTO {

    private UUID id;

    @NotNull(message = "Product ID is required")
    private UUID productDetailsId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private Boolean isAvailable;
}
