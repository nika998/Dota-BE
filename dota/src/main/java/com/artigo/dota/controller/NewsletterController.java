package com.artigo.dota.controller;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.service.NewsletterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping
    public ResponseEntity<NewsletterDTO> processNewsletter(@RequestBody@Valid NewsletterDTO newsletterDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(newsletterService.processNewsletter(newsletterDTO));
    }

    @GetMapping(value = "/unsubscribe/{uuid}")
    public String unsubscribeNewsletter(@PathVariable String uuid, Model model) {
        String message = newsletterService.logicalDeleteNewsletter(uuid);
        model.addAttribute("message", message);
        return "unsubscribeMessageTemplate";
    }
}
