package fr.ttis.npp.ipera.myreconsiliationapoi.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.mockito.Mockito.*;

class S3StorageServiceTest {

    @InjectMocks
    private S3StorageService s3StorageService;

    @Mock
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadReport() {
        String pspId = "1234";
        String reportDate = "2024-12-11";
        byte[] mockData = "mock content".getBytes();

        s3StorageService.uploadReport(pspId, reportDate, mockData);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any());
    }
}
