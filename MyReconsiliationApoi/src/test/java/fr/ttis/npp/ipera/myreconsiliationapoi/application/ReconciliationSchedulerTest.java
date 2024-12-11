package fr.ttis.npp.ipera.myreconsiliationapoi.application;


import fr.ttis.npp.ipera.myreconsiliationapoi.domain.service.ReportService;
import fr.ttis.npp.ipera.myreconsiliationapoi.domain.service.S3StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

class ReconciliationSchedulerTest {

    @InjectMocks
    private ReconciliationScheduler reconciliationScheduler;

    @Mock
    private EpiApiClient epiApiClient;

    @Mock
    private ReportService reportService;

    @Mock
    private S3StorageService s3StorageService;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndStoreReconciliationReport() throws Exception {
        String mockUrl = "https://mock-url.com";
        byte[] mockEncryptedReport = "mock encrypted data".getBytes();
        byte[] mockDecryptedReport = "mock decrypted data".getBytes();

        when(epiApiClient.getReportUrl(anyString(), anyString())).thenReturn(mockUrl);
        when(epiApiClient.downloadReport(mockUrl)).thenReturn(mockEncryptedReport);
        when(reportService.verifyAndExtractReport(mockEncryptedReport, any())).thenReturn(mockDecryptedReport);

        reconciliationScheduler.fetchAndStoreReconciliationReport();

        verify(s3StorageService, times(1)).uploadReport(anyString(), anyString(), eq(mockDecryptedReport));
    }
}
