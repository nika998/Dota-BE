package com.artigo.dota.repository;

import com.artigo.dota.entity.NewsletterDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsletterRepository extends JpaRepository<NewsletterDO, Long> {
    List<NewsletterDO> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
