package com.artigo.dota.service.impl;

import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.OrderExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderExportServiceImpl implements OrderExportService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final EmailService emailService;

    @Value("${excel.sheet.name:Orders}")
    private String excelSheetName;

    public OrderExportServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 10 * * *")//Every day at 10AM
    public void exportDailyOrdersExcel() {
        log.info("Daily orders report exporting started: " + LocalDateTime.now());
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        LocalDateTime endDate = LocalDateTime.now();

        List<OrderDO> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        try {
            generateExcelFile(orders,true);
        } catch (IOException e) {
            log.error("Could not generate daily orders excel file");
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 10 1 * *")
    public void exportMonthlyOrdersExcel() {
        log.info("Monthly orders report exporting started: " + LocalDateTime.now());
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<OrderDO> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        try {
            generateExcelFile(orders,false);
        } catch (IOException e) {
            log.error("Could not generate monthly orders excel file");
            e.printStackTrace();
        }
    }

    private void generateExcelFile(List<OrderDO> orders, boolean daily) throws IOException {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet(excelSheetName);

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            this.createHeaderRow(headerRow);

            for (OrderDO order : orders) {
                for (OrderItemDO orderItem : order.getOrderItems()) {
                    Row row = sheet.createRow(rowNum++);
                    this.fillOrderItemRow(row, order, orderItem);
                }
            }

            // Create temporary directory and file
            String excelFileName = "porudzbine_dnevni_izvestaj.xlsx";
            if(!daily) {
                excelFileName = "porudzbine_mesecni_izvestaj.xlsx";
            }
            Path tempDir = Files.createTempDirectory("excel_temp");
            String excelFilePath = tempDir.resolve(excelFileName).toString();
            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
                log.info("Orders excel file successfully created");
            }

            emailService.sendOrdersExcelMail(excelFilePath, daily, orders.isEmpty());
        }
    }

    private void createHeaderRow(Row headerRow) {
        String[] headers = {"ID porudžbine", "Ime i prezime", "Email", "Grad", "Poštanski broj", "Adresa", "Broj stana",
                "Telefon", "Opis", "Ukupna cena", "Datum kreiranja", "Tip dostave", "Dostupnsot", "Naziv prozivoda", "Tip proizvoda",
                "Količina", "Boja"};
        int colNum = 0;
        for (String header : headers) {
            Cell cell = headerRow.createCell(colNum++);
            cell.setCellValue(header);
        }
    }

    private void fillOrderItemRow(Row row, OrderDO order, OrderItemDO orderItem) {

        var product = productRepository.findById(orderItem.getProductDetails().getProductId());
        int colNum = 0;

        row.createCell(colNum++).setCellValue(order.getId());
        row.createCell(colNum++).setCellValue(order.getFullName());
        row.createCell(colNum++).setCellValue(order.getEmail());
        row.createCell(colNum++).setCellValue(order.getCity());
        row.createCell(colNum++).setCellValue(order.getPostalCode());
        row.createCell(colNum++).setCellValue(order.getAddress());
        row.createCell(colNum++).setCellValue(order.getFlatNumber());
        row.createCell(colNum++).setCellValue(order.getPhone());
        row.createCell(colNum++).setCellValue(order.getDescription());
        row.createCell(colNum++).setCellValue(order.getTotalPrice().doubleValue());
        row.createCell(colNum++).setCellValue(order.getCreatedAt().toString());
        row.createCell(colNum++).setCellValue(Boolean.TRUE.equals(order.getWaitReserved())? "Sačekati rezervisane proizvode" : "Dostaviti dostupne proizvode prvo");
        row.createCell(colNum++).setCellValue(Boolean.TRUE.equals(orderItem.getIsAvailable())? "Dostupno" : "Rezervisano");
        row.createCell(colNum++).setCellValue(product.isPresent()?product.get().getName():"Nepoznato");
        row.createCell(colNum++).setCellValue(product.isPresent()?product.get().getType():"Nepoznato");
        row.createCell(colNum++).setCellValue(orderItem.getQuantity());
        row.createCell(colNum).setCellValue(orderItem.getProductDetails().getInfo());
    }
}
