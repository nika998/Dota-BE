package com.artigo.dota.mapper;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.entity.OrderDO;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    OrderDO dtoToEntity(OrderDTO orderDTO);

    OrderDTO entityToDto(OrderDO orderDO);
}
