package fr.transactis.npp.ipera.dataprocessing.infra.service;

import fr.transactis.npp.ipera.dataprocessing.infra.adapter.EventServiceAdapter;
import fr.transactis.npp.ipera.dataprocessing.application.service.IFileProcessingService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class FileProcessingServiceImpl implements IFileProcessingService {

    private final EventServiceAdapter eventServiceAdapter;

    public FileProcessingServiceImpl(EventServiceAdapter eventServiceAdapter) {
        this.eventServiceAdapter = eventServiceAdapter;
    }

    @Retryable(
            value = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000)
    )
    @Override
    public void processFile(String filePath) {
        // Logique de traitement du fichier
        // Après le traitement, appeler le service d'événements
        eventServiceAdapter.saveEvent(filePath);
    }
}