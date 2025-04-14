package fr.ttis.npp.ipera.myreconsiliationapoi.application;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EpiApiClient {

    private final RestTemplate restTemplate;

    public EpiApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getReportUrl(String pspId, String reportDate) {
        String url = String.format("https://spi.int.epi.engineering/api/reconciliation/reports/psps/%s/%s", pspId, reportDate);
        return restTemplate.getForObject(url, String.class);
    }

    public byte[] downloadReport(String reportUrl) {
        return restTemplate.getForObject(reportUrl, byte[].class);
    }
}
