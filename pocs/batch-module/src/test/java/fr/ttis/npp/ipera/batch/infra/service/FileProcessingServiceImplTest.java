package fr.ttis.npp.ipera.batch.infra.service;

import fr.ttis.npp.ipera.batch.infra.adapter.EventServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileProcessingServiceImplTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EventServiceAdapter eventServiceAdapter;

    @InjectMocks
    private FileProcessingServiceImpl fileProcessingService;

    @Test
    public void processFileTest() {
        // Your test code here
    }
}