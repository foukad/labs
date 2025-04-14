package fr.ttis.npp.ipera.batch.infra.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventServiceAdapter {

    @Autowired
    private RestTemplate restTemplate;

    private final String EVENT_SERVICE_URL = "http://event-service/save-event";

    public void saveEvent(String filePath) {
        // Logique pour appeler le service d'événements d'un autre pod
        restTemplate.postForObject(EVENT_SERVICE_URL, filePath, Void.class);
    }
}
