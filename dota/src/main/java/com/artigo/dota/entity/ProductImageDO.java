package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

@Entity(name = "product_image")
@Data
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class ProductImageDO extends BaseEntity{

    @Column(name = "display")
    private Boolean isDisplay;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "product_detail_id", nullable = false)
    private Long productDetailId;

}
