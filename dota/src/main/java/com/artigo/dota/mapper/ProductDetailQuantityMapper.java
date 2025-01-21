package com.artigo.dota.mapper;

import com.artigo.dota.dto.ProductDetailQuantityDTO;
import com.artigo.dota.entity.ProductDetailsDO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductDetailQuantityMapper {

    ProductDetailQuantityDTO entityToDto(ProductDetailsDO productDetailsDO);
}
