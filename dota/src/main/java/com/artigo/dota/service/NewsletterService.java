package com.artigo.dota.service;

import com.artigo.dota.dto.NewsletterDTO;

public interface NewsletterService {

    NewsletterDTO processNewsletter(NewsletterDTO newsletterDTO);

    void exportAllSubsMadeToday();
}
