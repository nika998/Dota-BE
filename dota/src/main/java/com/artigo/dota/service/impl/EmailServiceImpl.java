package com.artigo.dota.service.impl;

import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
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
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    private final ProductRepository productRepository;

    @Value("${mail.order.arrived.subject}")
    private String orderArrivedMailSubject;
    @Value("${mail.order.excel.daily-subject}")
    private String orderExcelMailSubject;
    @Value("${mail.order.excel.monthly-subject}")
    private String orderExcelMonthlyMailSubject;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine, ProductRepository productRepository) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.productRepository = productRepository;
    }

    @Override
    public void sendOrderMail(List<String> recipientList, OrderDO order) {
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
            context.setVariable("order", order);
            context.setVariable("productsFromOrder", productsFromOrder);
            String htmlContent = templateEngine.process("newOrderEmailTemplate", context);

            helper.setText(htmlContent, true); // Set the HTML content

            emailSender.send(message);
            log.info("Mail with saved order successfully sent");
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
            log.info("Mail with orders report successfully sent");
        } catch (MessagingException e) {
            log.error("Could not send an email");
            e.printStackTrace();
        }
    }
}
