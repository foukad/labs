package com.example;

import fr.ttis.npp.ipera.myreconsiliationapoi.domain.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    @Mock
    private IS3StorageService s3StorageService;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyDecryptAndExtractReport_Success() throws Exception {
        // Arrange
        byte[] encryptedData = "encryptedData".getBytes();
        byte[] decryptedData = "decryptedData".getBytes();
        byte[] extractedFile = "extractedFile".getBytes();

        // Mock les méthodes internes
        ReportService spyReportService = spy(reportService);
        doReturn(decryptedData).when(spyReportService).decryptReport(encryptedData);
        doReturn(extractedFile).when(spyReportService).extractGzip(decryptedData);

        // Act
        spyReportService.verifyDecryptAndExtractReport(encryptedData);

        // Assert
        verify(spyReportService, times(1)).decryptReport(encryptedData);
        verify(spyReportService, times(1)).extractGzip(decryptedData);
        verify(s3StorageService, times(1)).uploadReport(eq(extractedFile), anyString());
    }

    @Test
    void testVerifyDecryptAndExtractReport_DecryptException() throws Exception {
        // Arrange
        byte[] encryptedData = "encryptedData".getBytes();

        // Mock la méthode decryptReport pour lancer une exception
        ReportService spyReportService = spy(reportService);
        doThrow(new RuntimeException("Decryption failed")).when(spyReportService).decryptReport(encryptedData);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            spyReportService.verifyDecryptAndExtractReport(encryptedData);
        });

        assertEquals("Decryption failed", exception.getMessage());
        verify(spyReportService, times(1)).decryptReport(encryptedData);
        verify(spyReportService, never()).extractGzip(any());
        verify(s3StorageService, never()).uploadReport(any(), anyString());
    }

    @Test
    void testVerifyDecryptAndExtractReport_ExtractException() throws Exception {
        // Arrange
        byte[] encryptedData = "encryptedData".getBytes();
        byte[] decryptedData = "decryptedData".getBytes();

        // Mock les méthodes internes
        ReportService spyReportService = spy(reportService);
        doReturn(decryptedData).when(spyReportService).decryptReport(encryptedData);
        doThrow(new RuntimeException("Extraction failed")).when(spyReportService).extractGzip(decryptedData);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            spyReportService.verifyDecryptAndExtractReport(encryptedData);
        });

        assertEquals("Extraction failed", exception.getMessage());
        verify(spyReportService, times(1)).decryptReport(encryptedData);
        verify(spyReportService, times(1)).extractGzip(decryptedData);
        verify(s3StorageService, never()).uploadReport(any(), anyString());
    }
}
