package com.artigo.dota.mapper;

import com.artigo.dota.dto.ProductDetailsDTO;
import com.artigo.dota.dto.ProductDetailsSubmitDTO;
import com.artigo.dota.entity.ProductDetailsDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductDetailsMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    ProductDetailsDO dtoToEntity(ProductDetailsDTO productDetailsDTO);

    ProductDetailsDTO entityToDto(ProductDetailsDO productDetailsDO);

    default ProductDetailsDTO submitDtoToDto(ProductDetailsSubmitDTO productDetailsSubmitDTO){
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        if(productDetailsSubmitDTO.getId() != null) {
            productDetailsDTO.setId(productDetailsSubmitDTO.getId());
        }
        productDetailsDTO.setColor(productDetailsSubmitDTO.getColor());
        productDetailsDTO.setQuantity(productDetailsSubmitDTO.getQuantity());
        productDetailsDTO.setInfo(productDetailsSubmitDTO.getInfo());
        productDetailsDTO.setProductId(productDetailsSubmitDTO.getProductId());
        return productDetailsDTO;
    }

    default Boolean mapIsDeleted() {
        return false;
    }
}
