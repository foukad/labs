package fr.ttis.npp.ipera.batch.controller;

import fr.ttis.npp.ipera.batch.application.service.IFileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileWatcherController {

    @Autowired
    private IFileProcessingService fileProcessingService;

    @PostMapping("/process-file")
    public void processFile(@RequestBody String filePath) {
        fileProcessingService.processFile(filePath);
    }
}