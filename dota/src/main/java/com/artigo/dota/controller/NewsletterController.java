package com.artigo.dota.controller;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.service.NewsletterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping
    public ResponseEntity<?> processNewsletter(@RequestBody@Valid NewsletterDTO newsletterDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsletterService.processNewsletter(newsletterDTO));
    }
}
