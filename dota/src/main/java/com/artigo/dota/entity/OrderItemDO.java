package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

@Entity(name = "order_item")
@Data
@Where(clause = "deleted = false")
public class OrderItemDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductDO product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "color")
    private String color;

    @Column(name = "deleted")
    private Boolean isDeleted;
}
