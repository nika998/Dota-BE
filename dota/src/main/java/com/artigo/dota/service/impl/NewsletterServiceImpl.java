package com.artigo.dota.service.impl;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.entity.NewsletterDO;
import com.artigo.dota.mapper.NewsletterMapper;
import com.artigo.dota.repository.NewsletterRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.NewsletterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;

    private final EmailService emailService;

    private final NewsletterMapper newsletterMapper;

    public NewsletterServiceImpl(NewsletterRepository newsletterRepository, EmailService emailService, NewsletterMapper newsletterMapper) {
        this.newsletterRepository = newsletterRepository;
        this.emailService = emailService;
        this.newsletterMapper = newsletterMapper;
    }

    @Override
    @Transactional
    public NewsletterDTO processNewsletter(NewsletterDTO newsletterDTO) {
        var newsletterDO = newsletterMapper.dtoToEntity(newsletterDTO);
        newsletterDO.setCreatedAt(LocalDateTime.now());

        var savedNewsletterDO = newsletterRepository.save(newsletterDO);

        return newsletterMapper.entityToDto(savedNewsletterDO);
    }

    @Override
    @Scheduled(cron = "0 0 12 * * *")//Every day at 12PM
    public void exportAllSubsMadeToday() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        LocalDateTime endDate = LocalDateTime.now();

        List<NewsletterDO> newsLetters = newsletterRepository.findByCreatedAtBetween(startDate, endDate);

        emailService.sendNewsletterMail(newsLetters);
    }
}
