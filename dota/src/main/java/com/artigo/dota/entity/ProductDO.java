package com.artigo.dota.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "product")
@Data
@Where(clause = "deleted = false")
public class ProductDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "deleted")
    private Boolean isDeleted;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private List<ProductDetailsDO> productDetails;

}
