package fr.ttis.npp.ipera.myreconsiliationapoi.domain.service;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.mail.smime.SMIMEEnvelopedParser;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.util.zip.GZIPInputStream;

@Service
public class ReportService {

    public byte[] verifyAndExtractReport(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        // Decrypt the report
        byte[] decryptedData = decryptReport(encryptedData, privateKey);

        // Extract .gz file
        return extractGzip(decryptedData);
    }

    private byte[] decryptReport(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        SMIMEEnvelopedParser parser = new SMIMEEnvelopedParser(new ByteArrayInputStream(encryptedData));
        return parser.getContent(privateKey);
    }

    private byte[] extractGzip(byte[] compressedData) throws Exception {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            return gzipInputStream.readAllBytes();
        }
    }
}
