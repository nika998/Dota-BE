package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity(name = "product_details")
@Data
@Where(clause = "deleted = false")
public class ProductDetailsDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color")
    private String color;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "info")
    private String info;

    @Column(name = "deleted")
    private Boolean isDeleted;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_detail_id")
    private List<ProductImageDO> images;
}
