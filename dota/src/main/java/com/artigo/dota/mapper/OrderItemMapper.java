package com.artigo.dota.mapper;

import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderItemMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    OrderItemDO dtoToEntity(OrderItemDTO orderItemDTO);
    OrderItemDTO entityToDto(OrderItemDO orderItemDO);

    default Boolean mapIsDeleted() {
        return false;
    }
}
