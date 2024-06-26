package fr.transactis.npp.ipera.dataprocessing.infra.service;

import fr.transactis.npp.ipera.dataprocessing.infra.adapter.EventServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

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