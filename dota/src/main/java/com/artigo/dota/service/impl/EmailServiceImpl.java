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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String CURRENCY= "RSD";

    private final EmailProperties emailProperties;

    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    private final ProductRepository productRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.newsletter.unsubscribe-path}")
    private String unsubscribePath;

    @Value("${aws.image-url}${app.logo-path}")
    private String logoUrl;

    @Value("${app.fe-home-path}")
    private String homePage;

    private final NumberFormat priceFormat;

    public EmailServiceImpl(EmailProperties emailProperties, JavaMailSender emailSender, TemplateEngine templateEngine, ProductRepository productRepository) {
        this.emailProperties = emailProperties;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.productRepository = productRepository;
        priceFormat = new DecimalFormat("0.00");
    }

    @Override
    public void sendOrderMails(OrderDO savedOrder) throws MailNotSentException {
        sendOrderMail(savedOrder, false);
        sendOrderMail(savedOrder, true);
    }

    @Override
    public void sendOrderMail(OrderDO order, boolean client) throws MailNotSentException {
        List<String> recipientList = new ArrayList<>();
        if (client) {
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
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getOrderArrivedMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("client", client);
            context.setVariable("order", order);
            context.setVariable("productsFromOrder", productsFromOrder);
            context.setVariable("logoUrl", logoUrl);
            context.setVariable("formattedPrice", priceFormat.format(order.getTotalPrice()) + " " + CURRENCY);
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
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }

            var context = new Context();
            context.setVariable("logoUrl", logoUrl);

            // Process the Thymeleaf template
            String emailContent;
            if (daily) {
                helper.setSubject(emailProperties.getOrderExcelMailSubject());
                if (!isExcelEmpty) {
                    emailContent = templateEngine.process("dailyOrdersEmailTemplate.html", context);
                } else {
                    emailContent = templateEngine.process("noOrdersEmailTemplate.html", context);
                }
            } else {
                helper.setSubject(emailProperties.getOrderExcelMonthlyMailSubject());
                if (!isExcelEmpty) {
                    emailContent = templateEngine.process("monthlyOrdersEmailTemplate.html", context);
                } else {
                    emailContent = templateEngine.process("noOrdersEmailTemplate.html", context);
                }
            }

            // Set the email content
            helper.setText(emailContent, true);
            // Attach the Excel file
            if (!isExcelEmpty) {
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
    public void sendContactMail(ContactFormDTO contactFormDTO) throws MailNotSentException {
        List<String> recipientList = emailProperties.getRecipients();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getContactFormMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("contactForm", contactFormDTO);
            context.setVariable("logoUrl", logoUrl);
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
    public void sendNewsletterMail(List<NewsletterDO> savedNewsletterDOs, List<String> unsubscribedEmails) {
        List<String> recipientList = emailProperties.getRecipients();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getNewsletterMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("newsletters", savedNewsletterDOs);
            context.setVariable("unsubscribedEmails", unsubscribedEmails);
            context.setVariable("logoUrl", logoUrl);
            String htmlContent = templateEngine.process("newsletterEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail with newsletter subscriptions successfully sent");
        } catch (RuntimeException | MessagingException e) {
            log.error("Could not send an email with newsletter subscriptions");
            e.printStackTrace();
        }
    }

    @Override
    public void sendNewsletterConformationMail(NewsletterDO newsletterDO) {
        List<String> recipientList = new ArrayList<>();
        recipientList.add(newsletterDO.getEmail());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getNewsletterConformationMailSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("newsletter", newsletterDO);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("unsubscribePath", unsubscribePath);
            context.setVariable("logoUrl", logoUrl);
            String htmlContent = templateEngine.process("newsletterConformationEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail with newsletter conformation successfully sent");
        } catch (RuntimeException | MessagingException e) {
            log.error("Could not send an email with newsletter conformation");
            e.printStackTrace();
        }
    }

    @Override
    public void sendNewsletterUnsubscribeMail(NewsletterDO deletedNewsLetter) {
        List<String> recipientList = new ArrayList<>();
        recipientList.add(deletedNewsLetter.getEmail());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }
            helper.setSubject(emailProperties.getNewsletterUnsubscribeSubject());

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("logoUrl", logoUrl);
            context.setVariable("homePage", homePage);
            String htmlContent = templateEngine.process("newsletterUnsubscribeEmailTemplate", context);
            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail for unsubscribed newsletter successfully sent");
        } catch (RuntimeException | MessagingException e) {
            log.error("Could not send an email for an unsubscribed newsletter");
            e.printStackTrace();
        }
    }

    @Override
    public void sendSubReportExcelMail(String excelFilePath, boolean isExcelEmpty) {
        List<String> recipientList = emailProperties.getRecipients();

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart support
            for (String recipient :
                    recipientList) {
                helper.setTo(recipient);
            }

            var context = new Context();
            context.setVariable("logoUrl", logoUrl);

            // Process the Thymeleaf template
            String emailContent;
            helper.setSubject(emailProperties.getNewsletterExcelMailSubject());
            if (!isExcelEmpty) {
                emailContent = templateEngine.process("subReportEmailTemplate.html", context);
            } else {
                emailContent = templateEngine.process("noSubReportEmailTemplate.html", context);
            }

            // Set the email content
            helper.setText(emailContent, true);
            // Attach the Excel file
            if (!isExcelEmpty) {
                File file = new File(excelFilePath);
                helper.addAttachment(file.getName(), file);
            }

            // Send the email
            emailSender.send(message);
            log.info("Mail with subscriptions report successfully sent");
        } catch (RuntimeException | MessagingException e) {
            log.error("Could not send an email");
            e.printStackTrace();
        }

    }

}
