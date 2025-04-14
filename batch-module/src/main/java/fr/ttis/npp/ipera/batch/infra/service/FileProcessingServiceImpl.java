package fr.ttis.npp.ipera.batch.infra.service;

import fr.ttis.npp.ipera.batch.application.service.IFileProcessingService;
import fr.ttis.npp.ipera.batch.infra.adapter.EventServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
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