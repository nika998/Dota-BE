package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity(name = "product_image")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductImageDO extends BaseEntity{

    @Column(name = "display")
    private Boolean isDisplay;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "product_detail_id", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID productDetailId;

}
