package com.artigo.dota.dto.converter;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductSubmitDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductSubmitToProductDTO {

    public ProductDTO convert(ProductSubmitDTO productSubmitDTO){
        ProductDTO productDTO = new ProductDTO();
        if(productSubmitDTO.getId() != null) {
            productDTO.setId(productSubmitDTO.getId());
        }
        productDTO.setName(productSubmitDTO.getName());
        productDTO.setInfo(productSubmitDTO.getInfo());
        productDTO.setType(productSubmitDTO.getType());
        productDTO.setSize(productSubmitDTO.getSize());
        productDTO.setPrice(productSubmitDTO.getPrice());
        productDTO.setQuantity(productSubmitDTO.getQuantity());
        return productDTO;
    }
}
