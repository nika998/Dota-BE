package com.artigo.dota.service;

import com.artigo.dota.dto.ContactFormDTO;

public interface ContactFormService {

    ContactFormDTO sendContactMessage(ContactFormDTO contactFormDTO);
}
