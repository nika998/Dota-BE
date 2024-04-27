package com.artigo.dota.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ContactFormDTO {

    @NotBlank(message = "Full name is required")
    @Length(max = 30, message = "Full name must be less than 30 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Length(max = 15, message = "Phone must be less than 15 characters")
    private String phone;

    @NotBlank(message = "Message is required")
    @Length(max = 1500, message = "Message must be less than 1500 characters")
    private String message;
}
