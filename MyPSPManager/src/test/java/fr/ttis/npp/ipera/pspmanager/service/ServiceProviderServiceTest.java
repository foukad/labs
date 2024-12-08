package fr.ttis.npp.ipera.pspmanager.service;

import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ServiceProviderServiceTest {

    @Mock
private RestTemplate restTemplate;

    @InjectMocks
    private ServiceProviderService serviceProviderService;

    @Test
    void testUpdateCertificateAndFetchPublicKey() {
        // Mock de la réponse de l'API
        PublicKey  publicKey = new PublicKey();
        publicKey.setN ("vZl9LV2l...");
        ServiceProviderResponse mockResponse = new ServiceProviderResponse ();
        mockResponse.setPublicKeys(List.of(publicKey));

        String serviceProviderId = "b47ee339-9575-4d08-9b7e-73b49097e823";
        String certificate = "MockCertificate";

        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.PUT),
                Mockito.any(HttpEntity.class),
                Mockito.eq(ServiceProviderResponse.class)
        )).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Appel du service
        String result = serviceProviderService.updateCertificateAndFetchPublicKey(serviceProviderId, certificate);

        // Vérification
        Assertions.assertEquals("vZl9LV2l...", result);
    }
}
