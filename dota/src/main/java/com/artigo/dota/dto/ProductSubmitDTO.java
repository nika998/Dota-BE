package com.artigo.dota.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductSubmitDTO {

    private Long id;
    private String name;
    private Double price;
    private String type;
    private int quantity;
    private String info;
    private String size;
    private List<ProductImageDTO> images;

}
