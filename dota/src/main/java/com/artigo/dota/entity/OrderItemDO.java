package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity(name = "order_item")
@Data
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class OrderItemDO extends BaseEntity{

    @Column(name = "order_id", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "product_details_id", nullable = false)
    private ProductDetailsDO productDetails;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "available")
    private Boolean isAvailable;

}
