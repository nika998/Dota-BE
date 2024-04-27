package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ContactFormDTO;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.service.ContactFormService;
import com.artigo.dota.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class ContactFormServiceImpl implements ContactFormService {

    private final EmailService emailService;

    public ContactFormServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public ContactFormDTO sendContactMessage(ContactFormDTO contactFormDTO) throws MailNotSentException{
        emailService.sendContactMail(contactFormDTO);
        return contactFormDTO;
    }
}
