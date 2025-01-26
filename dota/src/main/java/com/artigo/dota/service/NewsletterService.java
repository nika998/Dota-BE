package com.artigo.dota.service;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.entity.NewsletterDO;
import org.springframework.transaction.annotation.Transactional;

public interface NewsletterService {

    NewsletterDTO processNewsletter(NewsletterDTO newsletterDTO);

    NewsletterDO saveNewsletter(NewsletterDTO newsletterDTO);

    String logicalDeleteNewsletter(String uuid);

    void exportAllSubsMadeToday();

    void exportMonthlyExcelReport();
}
