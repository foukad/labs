package fr.ttis.npp.ipera.myreconsiliationapoi.domain.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadReport(String pspId, String reportDate, byte[] reportData) {
        String bucketName = "my-bucket";
        String key = String.format("reconciliation-reports/%s/%s/report.csv", pspId, reportDate);

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                new ByteArrayInputStream(reportData));
    }
}
