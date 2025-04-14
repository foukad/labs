package fr.ttis.npp.ipera.myreconsiliationapoi.application;


import fr.ttis.npp.ipera.myreconsiliationapoi.domain.service.ReportService;
import fr.ttis.npp.ipera.myreconsiliationapoi.domain.service.S3StorageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

@Component
public class ReconciliationScheduler {

    private final EpiApiClient epiApiClient;
    private final ReportService reportService;
    private final S3StorageService s3StorageService;
    private final PrivateKey privateKey;

    public ReconciliationScheduler(EpiApiClient epiApiClient, ReportService reportService,
                                   S3StorageService s3StorageService, PrivateKey privateKey) {
        this.epiApiClient = epiApiClient;
        this.reportService = reportService;
        this.s3StorageService = s3StorageService;
        this.privateKey = privateKey;
    }

    @Scheduled(cron = "${scheduler.cron}")
    public void fetchAndStoreReconciliationReport() {
        String pspId = "1234"; // Replace with parameterized value
        String reportDate = "2024-12-11"; // Replace with dynamic date

        try {
            String reportUrl = epiApiClient.getReportUrl(pspId, reportDate);
            byte[] encryptedReport = epiApiClient.downloadReport(reportUrl);
            byte[] reportData = reportService.verifyAndExtractReport(encryptedReport, privateKey);
            s3StorageService.uploadReport(pspId, reportDate, reportData);
        } catch (Exception e) {
            e.printStackTrace(); // Add proper logging
        }
    }
}
