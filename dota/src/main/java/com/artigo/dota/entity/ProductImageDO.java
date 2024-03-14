package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity(name = "product_image")
@Data
@Where(clause = "deleted = false")
public class ProductImageDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color")
    private String color;

    @Column(name = "display")
    private Boolean isDisplay;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "deleted")
    private Boolean isDeleted;
}
