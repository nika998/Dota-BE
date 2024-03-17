package com.artigo.dota.service.impl;

import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${mail.order.arrived.subject}")
    private String orderArrivedMailSubject;
    @Value("${mail.order.excel.daily-subject}")
    private String orderExcelMailSubject;
    @Value("${mail.order.excel.monthly-subject}")
    private String orderExcelMonthlyMailSubject;

    @Override
    public void sendOrderMail(List<String> recipientList, OrderDO order) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientList.toArray(new String[0]));
            helper.setSubject(orderArrivedMailSubject);

            // Process the HTML template with Thymeleaf
            Context context = new Context();
            context.setVariable("order", order);
            String htmlContent = templateEngine.process("newOrderEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Could not send an email");
            e.printStackTrace();
        }


    }

    @Override
    public void sendOrdersExcelMail(List<String> recipientList, String excelFilePath, boolean daily, boolean isExcelEmpty) {
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
        } catch (MessagingException e) {
            log.error("Could not send an email");
            e.printStackTrace();
        }
    }
}
