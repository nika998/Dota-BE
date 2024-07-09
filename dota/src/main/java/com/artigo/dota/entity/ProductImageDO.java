package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity(name = "product_image")
@Data
@EqualsAndHashCode(callSuper = true)
@FilterDef(name = "deletedImageFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedImageFilter", condition = "deleted = :isDeleted")
public class ProductImageDO extends BaseEntity{

    @Column(name = "display")
    private Boolean isDisplay;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "product_detail_id", nullable = false)
    private Long productDetailId;

}
