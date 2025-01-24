package com.artigo.dota.repository;

import com.artigo.dota.entity.OrderDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderDO, UUID> {
    List<OrderDO> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
