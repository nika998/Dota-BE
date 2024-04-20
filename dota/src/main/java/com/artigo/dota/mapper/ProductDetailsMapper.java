package com.artigo.dota.mapper;

import com.artigo.dota.dto.ProductDetailsDTO;
import com.artigo.dota.entity.ProductDetailsDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductDetailsMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    ProductDetailsDO dtoToEntity(ProductDetailsDTO productDetailsDTO);

    ProductDetailsDTO entityToDto(ProductDetailsDO productDetailsDO);

    default Boolean mapIsDeleted() {
        return false;
    }
}
