package com.artigo.dota.service;

import com.artigo.dota.entity.OrderDO;

import java.util.List;

public interface EmailService {

    void sendOrderMail(List<String> recipientList, OrderDO order);

    void sendOrdersExcelMail(List<String> recipientList, String excelFilePath, boolean daily, boolean isEmptyExcel);
}
