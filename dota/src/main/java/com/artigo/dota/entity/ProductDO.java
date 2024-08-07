package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "product")
@Data
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class ProductDO extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private String size;

    @Column(name = "new_collection")
    private Boolean isNewCollection;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @Where(clause = "deleted = false")
    private List<ProductDetailsDO> productDetails;

}
