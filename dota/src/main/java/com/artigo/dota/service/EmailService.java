package com.artigo.dota.service;

import java.util.List;
import java.util.Map;

public interface EmailService {

    void sendOrderMail(List<String> recipientList, Map<String, String> orderInfo);
}
