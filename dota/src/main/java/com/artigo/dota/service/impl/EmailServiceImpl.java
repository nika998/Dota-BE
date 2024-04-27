package com.artigo.dota.service.impl;

import com.artigo.dota.configuration.EmailProperties;
import com.artigo.dota.dto.ContactFormDTO;
import com.artigo.dota.entity.NewsletterDO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final EmailProperties emailProperties;

    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    private final ProductRepository productRepository;

    public EmailServiceImpl(EmailProperties emailProperties, JavaMailSender emailSender, TemplateEngine templateEngine, ProductRepository productRepository) {
        this.emailProperties = emailProperties;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.productRepository = productRepository;
    }

    @Override
    public void sendOrderMails(OrderDO savedOrder) {
        sendOrderMail(savedOrder, false);
        sendOrderMail(savedOrder, true);
    }

    @Override
    public void sendOrderMail(OrderDO order, boolean client) {
        List<String> recipientList = new ArrayList<>();
        if(client) {
            recipientList.add(order.getEmail());
        } else {
            recipientList = emailProperties.getRecipients();
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        var productsFromOrder = new HashMap<>();

        for (OrderItemDO orderItemDO :
                order.getOrderItems()) {
            var product = productRepository.findById(orderItemDO.getProductDetails().getProductId());
            product.ifPresent(productDO -> productsFromOrder.put(orderItemDO.getProductDetails().getProductId(), productDO));
        }

        try {
            for (String recipient:
                 recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getOrderArrivedMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("client", client);
            context.setVariable("order", order);
            context.setVariable("productsFromOrder", productsFromOrder);
            String htmlContent = templateEngine.process("newOrderEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail with saved order successfully sent");
        } catch (RuntimeException | MessagingException e) {
            e.printStackTrace();
            throw new MailNotSentException("Could not send email with saved order");
        }

    }

    @Override
    public void sendOrdersExcelMail(String excelFilePath, boolean daily, boolean isExcelEmpty) {
        List<String> recipientList = emailProperties.getRecipients();

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart support
            for (String recipient:
                    recipientList) {
                helper.setTo(recipient);
            }

            // Process the Thymeleaf template
            String emailContent;
            if(daily){
                helper.setSubject(emailProperties.getOrderExcelMailSubject());
                if(!isExcelEmpty) {
                    emailContent = templateEngine.process("dailyOrdersEmailTemplate.html", new Context());
                } else {
                    emailContent = templateEngine.process("noOrdersEmailTemplate.html", new Context());
                }
            } else {
                helper.setSubject(emailProperties.getOrderExcelMonthlyMailSubject());
                if (!isExcelEmpty) {
                    emailContent = templateEngine.process("monthlyOrdersEmailTemplate.html", new Context());
                } else {
                    emailContent = templateEngine.process("noOrdersEmailTemplate.html", new Context());
                }
            }

            // Set the email content
            helper.setText(emailContent, true);
            // Attach the Excel file
            if(!isExcelEmpty) {
                File file = new File(excelFilePath);
                helper.addAttachment(file.getName(), file);
            }

            // Send the email
            emailSender.send(message);
            log.info("Mail with orders report successfully sent");
        } catch (RuntimeException | MessagingException e) {
            log.error("Could not send an email");
            e.printStackTrace();
        }
    }

    @Override
    public void sendContactMail(ContactFormDTO contactFormDTO) {
        List<String> recipientList = emailProperties.getRecipients();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String recipient:
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getContactFormMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("contactForm", contactFormDTO);
            String htmlContent = templateEngine.process("contactFormEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail with new contact message successfully sent");
        } catch (RuntimeException | MessagingException e) {
            e.printStackTrace();
            throw new MailNotSentException("Could not mail contact message");
        }
    }

    @Override
    public void sendNewsletterMail(List<NewsletterDO> savedNewsletterDOs) {
        List<String> recipientList = emailProperties.getRecipients();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String recipient:
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getNewsletterMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("newsletters", savedNewsletterDOs);
            String htmlContent = templateEngine.process("newsletterEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail with newsletter subscriptions successfully sent");
        } catch (RuntimeException | MessagingException e) {
            log.error("Could not send an email");
            e.printStackTrace();
        }
    }

}
