package com.artigo.dota.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private Long id;
    private String fullName;
    private String email;
    private String city;
    private String postalCode;
    private String address;
    private String flatNumber;
    private String phone;
    private String description;
    private List<OrderItemDTO> orderItems;
    private double totalPrice;
}
