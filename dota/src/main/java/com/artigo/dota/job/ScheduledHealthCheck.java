package com.artigo.dota.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ScheduledHealthCheck {

    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    public ScheduledHealthCheck(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Scheduled(fixedRateString = "${app.health-check.rate}")
    public void checkHealth() {
        try {
            String url = baseUrl + "/dota/actuator/health";
            String response = restTemplate.getForObject(url, String.class);
            log.info("Health Check Response: " + response);
        } catch (Exception e) {
            log.error("Health Check Failed: " + e.getMessage());
        }
    }
}
