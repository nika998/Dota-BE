package com.artigo.dota.service;

import com.artigo.dota.dto.ContactFormDTO;
import com.artigo.dota.entity.NewsletterDO;
import com.artigo.dota.entity.OrderDO;

import java.util.List;

public interface EmailService {

    void sendOrderMails(OrderDO savedOrder);

    void sendOrderMail(OrderDO order, boolean client);

    void sendOrdersExcelMail(String excelFilePath, boolean daily, boolean isExcelEmpty);

    void sendContactMail(ContactFormDTO contactFormDTO);

    void sendNewsletterMail(List<NewsletterDO> savedNewsletterDO);
}
