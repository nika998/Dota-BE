package com.artigo.dota.controller;

import com.artigo.dota.dto.ContactFormDTO;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.service.ContactFormService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/contact")
public class ContactFormController {

    private final ContactFormService contactFormService;

    public ContactFormController(ContactFormService contactFormService) {
        this.contactFormService = contactFormService;
    }

    @PostMapping
    public ResponseEntity<?> sendContactMessage(@RequestBody @Valid ContactFormDTO contactFormDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(contactFormService.sendContactMessage(contactFormDTO));
        } catch (MailNotSentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
