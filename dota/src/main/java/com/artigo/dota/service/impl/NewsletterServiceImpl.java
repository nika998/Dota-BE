package com.artigo.dota.service.impl;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.entity.NewsletterDO;
import com.artigo.dota.mapper.NewsletterMapper;
import com.artigo.dota.repository.NewsletterRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.NewsletterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;

    private final EmailService emailService;

    private final NewsletterMapper newsletterMapper;

    @Value("${newsletter.unsubscribe.successful}")
    private String unsubscribeSuccessfulMessage;

    @Value("${newsletter.unsubscribe.unsuccessful}")
    private String unsubscribeUnsuccessfulMessage;

    public NewsletterServiceImpl(NewsletterRepository newsletterRepository, EmailService emailService, NewsletterMapper newsletterMapper) {
        this.newsletterRepository = newsletterRepository;
        this.emailService = emailService;
        this.newsletterMapper = newsletterMapper;
    }

    @Override
    @Transactional
    public NewsletterDTO processNewsletter(NewsletterDTO newsletterDTO) {
        Optional<NewsletterDO> existingNewsLetter = newsletterRepository.findByEmailAndIsDeletedFalse(newsletterDTO.getEmail());
        if(existingNewsLetter.isPresent()) {
            return newsletterMapper.entityToDtoExists(existingNewsLetter.get());
        }

        var newsletterDO = newsletterMapper.dtoToEntity(newsletterDTO);
        newsletterDO.setCreatedAt(LocalDateTime.now());
        newsletterDO.setUuid(UUID.randomUUID().toString());

        var savedNewsletterDO = newsletterRepository.save(newsletterDO);

        emailService.sendNewsletterConformationMail(newsletterDO);

        return newsletterMapper.entityToDtoSucces(savedNewsletterDO);
    }

    @Override
    @Transactional
    public String logicalDeleteNewsletter(String uuid) {
        Optional<NewsletterDO> existingNewsletter = newsletterRepository.findByUuidAndIsDeletedFalse(uuid);
        if(existingNewsletter.isPresent()) {
            NewsletterDO newsletterToDelete = existingNewsletter.get();
            newsletterToDelete.setIsDeleted(Boolean.TRUE);
            NewsletterDO deletedNewsLetter = newsletterRepository.save(newsletterToDelete);

            emailService.sendNewsletterUnsubscribeMail(deletedNewsLetter);
            return unsubscribeSuccessfulMessage;
        } else {
            return unsubscribeUnsuccessfulMessage;
        }
    }

    @Override
    @Scheduled(cron = "0 0 12 * * *")//Every day at 12PM
    @Transactional
    public void exportAllSubsMadeToday() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        LocalDateTime endDate = LocalDateTime.now();

        List<NewsletterDO> newsLetters = newsletterRepository.findByCreatedAtBetweenAndIsDeletedFalse(startDate, endDate);
        List<String> unsubscribedEmails = newsletterRepository.findDistinctEmailByCreatedAtBetweenAndIsDeletedTrue(startDate, endDate);

        emailService.sendNewsletterMail(newsLetters, unsubscribedEmails);
    }

}
