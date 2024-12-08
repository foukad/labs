package fr.ttis.npp.ipera.pspmanager.service;

@Service
public class ServiceProviderService {

    private final RestTemplate restTemplate;

    public ServiceProviderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String updateCertificateAndFetchPublicKey(String serviceProviderId, String certificate) {
        // Construire la requête PUT pour l'API externe
        HttpHeaders headers = new HttpHeaders();
        headers.set(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(certificate, headers);

        // URL de l'API externe
        String url = String.format("https://spi.int.epi.engineering/api/service-providers/%s", serviceProviderId);

        // Appel de l'API externe
        ResponseEntity<ServiceProviderResponse > response = restTemplate.exchange(
                url, HttpMethod.PUT, request, ServiceProviderResponse  .class);

        // Extraire la clé publique de la réponse
        return response.getBody()
                .getPublicKeys()
                .stream()
                .findFirst()
                .map(PublicKey ::getN) // La clé publique est contenue dans le champ "n"
                .orElseThrow(() -> new RuntimeException("Public key not found in response"));
    }
}
