package fr.ttis.npp.ipera.notification.infra.service;


import fr.ttis.npp.ipera.notification.application.service.IFileNotificationService;
import fr.ttis.npp.ipera.notification.infra.adapter.EventServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileNotificationServiceImpl implements IFileNotificationService {

    @Autowired
    private EventServiceAdapter eventServiceAdapter;

    @Override
    public void processFile(String filePath) {
        // Logique de traitement du fichier
        // Après le traitement, appeler le service d'événements
        eventServiceAdapter.saveEvent(filePath);
    }
}