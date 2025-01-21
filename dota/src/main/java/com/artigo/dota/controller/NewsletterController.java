package com.artigo.dota.controller;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.service.NewsletterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/newsletter")
@Slf4j
public class NewsletterController {

    private final NewsletterService newsletterService;

    @Value("${app.fe-home-path}")
    private String homePage;

    @Value("${aws.image-url}${app.logo-path}")
    private String logoUrl;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping
    public ResponseEntity<NewsletterDTO> processNewsletter(@RequestBody@Valid NewsletterDTO newsletterDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(newsletterService.processNewsletter(newsletterDTO));
    }

    @GetMapping(value = "/unsubscribe/{uuid}")
    public String unsubscribeNewsletter(@PathVariable String uuid, Model model) {
        log.info(newsletterService.logicalDeleteNewsletter(uuid));
        model.addAttribute("homePage", homePage);
        model.addAttribute("logoUrl", logoUrl);
        return "unsubscribeMessageTemplate";
    }
}
