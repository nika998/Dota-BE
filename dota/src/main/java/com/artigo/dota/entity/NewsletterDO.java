package com.artigo.dota.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "newsletter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NewsletterDO extends BaseEntity{

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String uuid;

}
