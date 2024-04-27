package com.artigo.dota.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;

    @NotBlank(message = "Full name is required")
    @Length(max = 30, message = "Full name must be less than 30 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "City is required")
    @Length(max = 30, message = "City name must be less than 30 characters")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{5}", message = "Postal code must be 5 digits")
    private String postalCode;

    @NotBlank(message = "Address is required")
    @Length(max = 30, message = "Address must be less than 30 characters")
    private String address;

    @Length(max = 30, message = "Flat number must be less than 30 characters")
    private String flatNumber;

    @NotBlank(message = "Phone number is required")
    @Length(max = 15, message = "Phone must be less than 15 characters")
    private String phone;

    private String description;

    @Valid
    @NotNull(message = "Order items are required")
    private List<OrderItemDTO> orderItems;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.01", message = "Total price must be greater than or equal to 0.01")
    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    @NotNull(message = "Choose wait reserved items option")
    private Boolean waitReserved;
}
