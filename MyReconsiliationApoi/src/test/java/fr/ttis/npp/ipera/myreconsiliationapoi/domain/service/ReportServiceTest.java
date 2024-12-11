package fr.ttis.npp.ipera.myreconsiliationapoi.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    private byte[] compressedData;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws Exception {
        // Generate a key pair for testing
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyPair = keyGen.generateKeyPair();

        // Compress mock data
        byte[] mockData = "test report content".getBytes();
        compressedData = compressData(mockData);
    }

    @Test
    void testVerifyAndExtractReport() throws Exception {
        byte[] extractedData = reportService.verifyAndExtractReport(compressedData, keyPair.getPrivate());
        assertNotNull(extractedData);
        assertEquals("test report content", new String(extractedData));
    }

    private byte[] compressData(byte[] data) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOut = new GZIPOutputStream(baos)) {
            gzipOut.write(data);
            gzipOut.close();
            return baos.toByteArray();
        }
    }
}
