package com.artigo.dota.dto;

import java.util.List;

public record OrderDTO(
        Long id,
        String fullName,
        String email,
        String city,
        String postalCode,
        String address,
        String flatNumber,
        String phone,
        String description,
        List<ProductDTO> products,
        double totalPrice
) {}
