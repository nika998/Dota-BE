package com.artigo.dota.repository;

import com.artigo.dota.entity.OrderDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderDO, Long> {
    List<OrderDO> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
