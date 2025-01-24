package com.artigo.dota.repository;

import com.artigo.dota.entity.OrderItemDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemDO, UUID> {
}
