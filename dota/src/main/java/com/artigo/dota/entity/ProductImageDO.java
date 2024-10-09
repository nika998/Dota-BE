package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "product_image")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductImageDO extends BaseEntity{

    @Column(name = "display")
    private Boolean isDisplay;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "product_detail_id", nullable = false)
    private Long productDetailId;

}
