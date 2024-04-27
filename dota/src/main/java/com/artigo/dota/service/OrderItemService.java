package com.artigo.dota.service;

import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDO> saveAll(List<OrderItemDO> orderItemDOs);

    OrderDO getRelatedOrder(OrderItemDO orderItemDO);
}
