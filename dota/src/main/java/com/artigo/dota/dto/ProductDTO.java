package com.artigo.dota.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

        private Long id;
        private String name;
        private Double price;
        private String type;
        private int quantity;
        private String info;
        private String size;
        private List<ProductImageDTO> images;

}
