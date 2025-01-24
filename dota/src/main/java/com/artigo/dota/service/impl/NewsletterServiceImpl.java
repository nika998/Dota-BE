package com.artigo.dota.service.impl;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.entity.NewsletterDO;
import com.artigo.dota.mapper.NewsletterMapper;
import com.artigo.dota.repository.NewsletterRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.NewsletterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;

    private final EmailService emailService;

    private final NewsletterMapper newsletterMapper;

    @Value("${newsletter.unsubscribe.successful}")
    private String unsubscribeSuccessfulMessage;

    @Value("${newsletter.unsubscribe.unsuccessful}")
    private String unsubscribeUnsuccessfulMessage;

    public NewsletterServiceImpl(NewsletterRepository newsletterRepository, EmailService emailService, NewsletterMapper newsletterMapper) {
        this.newsletterRepository = newsletterRepository;
        this.emailService = emailService;
        this.newsletterMapper = newsletterMapper;
    }

    @Override
    @Transactional
    public NewsletterDTO processNewsletter(NewsletterDTO newsletterDTO) {
        Optional<NewsletterDO> existingNewsLetter = newsletterRepository.findByEmailAndIsDeletedFalse(newsletterDTO.getEmail());
        if(existingNewsLetter.isPresent()) {
            return newsletterMapper.entityToDtoExists(existingNewsLetter.get());
        }

        var newsletterDO = newsletterMapper.dtoToEntity(newsletterDTO);
        newsletterDO.setCreatedAt(LocalDateTime.now());
        newsletterDO.setUuid(UUID.randomUUID().toString());

        var savedNewsletterDO = newsletterRepository.save(newsletterDO);

        emailService.sendNewsletterConformationMail(newsletterDO);

        return newsletterMapper.entityToDtoSucces(savedNewsletterDO);
    }

    @Override
    @Transactional
    public String logicalDeleteNewsletter(String uuid) {
        Optional<NewsletterDO> existingNewsletter = newsletterRepository.findByUuidAndIsDeletedFalse(uuid);
        if(existingNewsletter.isPresent()) {
            NewsletterDO newsletterToDelete = existingNewsletter.get();
            newsletterToDelete.setIsDeleted(Boolean.TRUE);
            NewsletterDO deletedNewsLetter = newsletterRepository.save(newsletterToDelete);

            emailService.sendNewsletterUnsubscribeMail(deletedNewsLetter);
            return unsubscribeSuccessfulMessage;
        } else {
            return unsubscribeUnsuccessfulMessage;
        }
    }

    @Override
    @Scheduled(cron = "0 0 10 * * *")//Every day at 10AM
    @Transactional
    public void exportAllSubsMadeToday() {
        LocalDateTime startDate = LocalDateTime.now().minusHours(24);
        LocalDateTime endDate = LocalDateTime.now();

        List<NewsletterDO> newsLetters = newsletterRepository.findByCreatedAtBetweenAndIsDeletedFalse(startDate, endDate);
        List<String> unsubscribedEmails = newsletterRepository.findDistinctEmailByCreatedAtBetweenAndIsDeletedTrue(startDate, endDate);

        emailService.sendNewsletterMail(newsLetters, unsubscribedEmails);
    }

    @Override
    @Scheduled(cron = "0 0 10 1 * *")
    public void exportMonthlyExcelReport() {
        var startDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        var endDate = LocalDateTime.now().withDayOfMonth(31).withHour(0).withMinute(0).withSecond(0);

        try {
            generateExcelFile(newsletterRepository.findByCreatedAtBetweenAndIsDeletedFalse(startDate, endDate));
        } catch (IOException e) {
            log.error("Could not generate monthly subscriptions excel file");
            e.printStackTrace();
        }
    }

    private void generateExcelFile(List<NewsletterDO> newsLetters) throws IOException {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Subscriptions");

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            this.createHeaderRow(headerRow);

            for (NewsletterDO newsletter : newsLetters) {
                Row row = sheet.createRow(rowNum++);
                this.fillNewsletterItemRow(row, newsletter);
            }

            String excelFileName = "mesecne_newsletter_pretplate.xlsx";
            Path tempDir = Files.createTempDirectory("sub_excel_temp");
            String excelFilePath = tempDir.resolve(excelFileName).toString();
            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
            }

            emailService.sendSubReportExcelMail(excelFilePath, newsLetters.isEmpty());
        }
    }

    private void createHeaderRow(Row headerRow) {
        String[] headers = {"ID pretplate", "Email", "Datum pretplate"};
        int colNum = 0;
        for (String header : headers) {
            Cell cell = headerRow.createCell(colNum++);
            cell.setCellValue(header);
        }
    }

    private void fillNewsletterItemRow(Row row, NewsletterDO newsletter) {
        int colNum = 0;

        row.createCell(colNum++).setCellValue(newsletter.getId().toString());
        row.createCell(colNum++).setCellValue(newsletter.getEmail());
        row.createCell(colNum).setCellValue(newsletter.getCreatedAt().toString());
    }

}
