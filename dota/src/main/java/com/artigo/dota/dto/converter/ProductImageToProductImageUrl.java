package com.artigo.dota.dto.converter;

import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductImageToProductImageUrl {

    public ProductImageUrlDTO convert(ProductImageDTO productImageDTO, String productImageUrl) {
        ProductImageUrlDTO productImageUrlDTO = new ProductImageUrlDTO();
        if(productImageDTO.getId() != null) {
            productImageUrlDTO.setId(productImageDTO.getId());
        }
        productImageUrlDTO.setIsDisplay(productImageDTO.getIsDisplay());
        productImageUrlDTO.setColor(productImageDTO.getColor());
        productImageUrlDTO.setImagePath(productImageUrl);

        return productImageUrlDTO;
    }
}
