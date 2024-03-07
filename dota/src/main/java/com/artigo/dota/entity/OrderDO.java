package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "order_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    private String city;
    @Column(name = "postal_code")
    private String postalCode;
    private String address;
    @Column(name = "flat_number")
    private String flatNumber;
    private String phone;

    private String description;

    @OneToMany
    private List<ProductDO> products;

    @Column(name = "total_price")
    private double totalPrice;

}
