package com.artigo.dota.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {

        private Long id;

        @NotBlank(message = "Name is required")
        @Size(max = 30, message = "Name must be less than or equal to 30 characters")
        private String name;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
        private BigDecimal price;

        @NotBlank(message = "Type is required")
        private String type;

        private String size;

        @Valid
        @NotNull(message = "Product details are required")
        private List<ProductDetailsDTO> productDetails;
}
