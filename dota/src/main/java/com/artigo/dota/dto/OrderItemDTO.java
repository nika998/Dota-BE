package com.artigo.dota.dto;

import lombok.Data;

@Data
public class OrderItemDTO {

    private Long id;
    private Long order;
    private ProductDTO product;
    private int quantity;
    private String color;
}
