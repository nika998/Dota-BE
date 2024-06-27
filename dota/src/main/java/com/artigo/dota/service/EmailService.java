package com.artigo.dota.service;

import com.artigo.dota.dto.ContactFormDTO;
import com.artigo.dota.entity.NewsletterDO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.exception.MailNotSentException;

import java.util.List;

public interface EmailService {

    void sendOrderMails(OrderDO savedOrder) throws MailNotSentException;

    void sendOrderMail(OrderDO order, boolean client) throws MailNotSentException;

    void sendOrdersExcelMail(String excelFilePath, boolean daily, boolean isExcelEmpty);

    void sendContactMail(ContactFormDTO contactFormDTO) throws MailNotSentException;

    void sendNewsletterMail(List<NewsletterDO> savedNewsletterDO);
}
