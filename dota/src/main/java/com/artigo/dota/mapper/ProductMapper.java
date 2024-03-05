package com.artigo.dota.mapper;
import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.entity.ProductDO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductDO dtoToEntity(ProductDTO productDTO);

    ProductDTO entityToDto(ProductDO product);

}
