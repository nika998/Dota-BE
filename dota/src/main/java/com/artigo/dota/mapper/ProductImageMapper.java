package com.artigo.dota.mapper;

import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductImageDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductImageMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    ProductImageDO dtoToEntity(ProductImageUrlDTO productImageDTO);
    ProductImageUrlDTO entityToDto(ProductImageDO productImage);

    default Boolean mapIsDeleted() {
        return false;
    }
}
