package com.artigo.dota.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity(name = "newsletter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class NewsletterDO extends BaseEntity{

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
