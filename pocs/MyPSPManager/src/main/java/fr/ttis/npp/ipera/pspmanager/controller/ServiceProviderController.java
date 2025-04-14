package fr.ttis.npp.ipera.pspmanager.controller;

import fr.ttis.npp.ipera.pspmanager.service.ServiceProviderService;

@RestController
@RequestMapping("/api/service-providers")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @PutMapping("/{serviceProviderId}/certificate")
    public ResponseEntity<String> updateCertificate(
            @PathVariable String serviceProviderId,
            @RequestBody String reconciliationReportEncryptionCertificate) {
        String publicKey = serviceProviderService.updateCertificateAndFetchPublicKey(
                serviceProviderId, reconciliationReportEncryptionCertificate);
        return ResponseEntity.ok(publicKey);
    }
}

