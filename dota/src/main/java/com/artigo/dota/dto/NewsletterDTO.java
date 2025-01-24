package com.artigo.dota.dto;

import com.artigo.dota.dto.enums.NewsletterStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NewsletterDTO {

    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private LocalDateTime createdAt;

    private NewsletterStatus status;

}
