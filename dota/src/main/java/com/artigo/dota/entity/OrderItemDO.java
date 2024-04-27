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
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "product_details_id", nullable = false)
    private ProductDetailsDO productDetails;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "available")
    private Boolean isAvailable;

    @Column(name = "deleted")
    private Boolean isDeleted;
}
