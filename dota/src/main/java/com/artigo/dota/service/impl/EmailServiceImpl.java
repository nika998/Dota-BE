package com.artigo.dota.service.impl;

import com.artigo.dota.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.order.arrived.subject}")
    private String orderArrivedMailSubject;

    @Override
    public void sendOrderMail(List<String> recipientList, Map<String, String> orderInfo) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientList.toArray(new String[0]));
            helper.setSubject(orderArrivedMailSubject);

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("title", orderInfo.get("title"));
//            context.setVariable("content", content);
            String htmlContent = templateEngine.process("newOrderEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
        } catch (MessagingException e) {
            // Handle exception
            e.printStackTrace();
        }


    }
}
