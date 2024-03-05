package com.artigo.dota.mapper;

import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.entity.ProductImageDO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductImageMapper {

    ProductImageDO dtoToEntity(ProductImageDTO productImageDTO);

    ProductImageDTO entityToDto(ProductImageDO productImage);
}
