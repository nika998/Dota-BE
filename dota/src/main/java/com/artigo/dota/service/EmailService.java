package com.artigo.dota.service;

import com.artigo.dota.entity.OrderDO;

import java.util.List;
import java.util.Map;

public interface EmailService {

    void sendOrderMail(List<String> recipientList, OrderDO order);
}
