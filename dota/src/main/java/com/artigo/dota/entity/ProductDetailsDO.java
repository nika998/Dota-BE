package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity(name = "product_details")
@Data
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class ProductDetailsDO extends BaseEntity{

    @Column(name = "color")
    private String color;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "info")
    private String info;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_detail_id")
    private List<ProductImageDO> images;
}
