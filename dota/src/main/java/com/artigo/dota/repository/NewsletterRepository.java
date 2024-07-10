package com.artigo.dota.repository;

import com.artigo.dota.entity.NewsletterDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsletterRepository extends JpaRepository<NewsletterDO, Long> {
    List<NewsletterDO> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT DISTINCT n.email FROM newsletter n WHERE n.createdAt BETWEEN :startDate AND :endDate AND n.isDeleted = true")
    List<String> findDistinctEmailByCreatedAtBetweenAndIsDeletedTrue(@Param("startDate") LocalDateTime startDate,
                                                                     @Param("endDate") LocalDateTime endDate);

    Optional<NewsletterDO> findByEmailAndIsDeletedFalse(String email);

    Optional<NewsletterDO> findByUuidAndIsDeletedFalse(String uuid);
}
