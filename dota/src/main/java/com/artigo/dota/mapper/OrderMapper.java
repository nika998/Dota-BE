package com.artigo.dota.mapper;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.ProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    OrderDO dtoToEntity(OrderDTO orderDTO);
    OrderDTO entityToDto(OrderDO orderDO);

    @Mapping(target = "images", ignore = true) // Ignore mapping images list
    ProductDTO productDOToProductDTO(ProductDO productDO);

    default Boolean mapIsDeleted() {
        return false;
    }
}
