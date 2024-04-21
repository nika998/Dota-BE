package com.artigo.dota.mapper;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.entity.OrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    OrderDO dtoToEntity(OrderDTO orderDTO);

    OrderDTO entityToDto(OrderDO orderDO);

    default Boolean mapIsDeleted() {
        return false;
    }
}
