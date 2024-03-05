package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "product_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductDO product;

    @Column(name = "color")
    private String color;

    @Column(name = "display")
    private boolean isDisplay;

    @Column(name = "image_path")
    private String imagePath;


}
