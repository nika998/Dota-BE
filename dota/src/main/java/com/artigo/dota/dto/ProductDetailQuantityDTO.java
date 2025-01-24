package com.artigo.dota.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDetailQuantityDTO {

    private UUID id;
    
    private int quantity;
}
