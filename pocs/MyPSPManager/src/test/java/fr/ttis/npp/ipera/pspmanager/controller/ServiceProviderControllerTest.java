package fr.ttis.npp.ipera.pspmanager.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Autowired
private MockMvc mockMvc;

@MockBean
private PublicKeyService publicKeyService;

@Test
void shouldUpdateReconciliationCertificateAndReturnPublicKey() throws Exception {
    // Données simulées
    Long serviceProviderId = 1L;
    String newCertificate = """
            -----BEGIN CERTIFICATE-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAxY3JUJlf6...
            -----END CERTIFICATE-----
            """;
    String expectedPublicKey = "MIIBIjANBgkqhkiG...";

    // Mock du service
    Mockito.when(publicKeyService.updateCertificateAndGetPublicKey(serviceProviderId, newCertificate))
            .thenReturn(expectedPublicKey);

    // Requête PUT
    mockMvc.perform(put("/api/service-providers/{id}/certificate", serviceProviderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(newCertificate))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.publicKey").value(expectedPublicKey));

    // Vérification
    Mockito.verify(publicKeyService).updateCertificateAndGetPublicKey(serviceProviderId, newCertificate);
}

p