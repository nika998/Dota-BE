package com.artigo.dota.service;

import com.artigo.dota.dto.ContactFormDTO;
import com.artigo.dota.exception.MailNotSentException;

public interface ContactFormService {

    ContactFormDTO sendContactMessage(ContactFormDTO contactFormDTO) throws MailNotSentException;
}
