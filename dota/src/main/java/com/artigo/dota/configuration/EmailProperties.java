package com.artigo.dota.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class EmailProperties {

    private List<String> recipients;

    @Value("${mail.order.arrived.subject}")
    private String orderArrivedMailSubject;

    @Value("${mail.order.excel.daily-subject}")
    private String orderExcelMailSubject;

    @Value("${mail.order.excel.monthly-subject}")
    private String orderExcelMonthlyMailSubject;

    @Value("${mail.contact-form.subject}")
    private String contactFormMailSubject;

    @Value("${mail.newsletter.subject}")
    private String newsletterMailSubject;

    @Value("${mail.newsletter.conformation.subject}")
    private String newsletterConformationMailSubject;

    @Value("${mail.newsletter.unsubscribe.subject}")
    private String newsletterUnsubscribeSubject;
}
