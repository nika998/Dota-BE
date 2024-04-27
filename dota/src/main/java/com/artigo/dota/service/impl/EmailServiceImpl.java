package com.artigo.dota.service.impl;

import com.artigo.dota.dto.ContactFormDTO;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    private final ProductRepository productRepository;

    @Value("${spring.mail.username}")
    private String mailDefaultRecipient;

    @Value("${mail.order.arrived.subject}")
    private String orderArrivedMailSubject;

    @Value("${mail.order.excel.daily-subject}")
    private String orderExcelMailSubject;

    @Value("${mail.order.excel.monthly-subject}")
    private String orderExcelMonthlyMailSubject;

    @Value("${mail.contact-form.subject}")
    private String contactFormMailSubject;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine, ProductRepository productRepository) {
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
            recipientList.add(mailDefaultRecipient);
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
            helper.setTo(recipientList.toArray(new String[0]));
            helper.setSubject(orderArrivedMailSubject);

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
        List<String> recipientList = new ArrayList<>(Collections.singletonList(mailDefaultRecipient));

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart support
            helper.setTo(recipientList.toArray(new String[0]));

            // Process the Thymeleaf template
            String emailContent;
            if(daily){
                helper.setSubject(orderExcelMailSubject);
                if(!isExcelEmpty) {
                    emailContent = templateEngine.process("dailyOrdersEmailTemplate.html", new Context());
                } else {
                    emailContent = templateEngine.process("noOrdersEmailTemplate.html", new Context());
                }
            } else {
                helper.setSubject(orderExcelMonthlyMailSubject);
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
        List<String> recipientList = new ArrayList<>();
        recipientList.add(mailDefaultRecipient);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientList.toArray(new String[0]));
            helper.setSubject(contactFormMailSubject);

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

}
