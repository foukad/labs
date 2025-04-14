package fr.ttis.npp.ipera.notification.controller;

import fr.ttis.npp.ipera.notification.application.service.IFileNotificationService;
import fr.ttis.npp.ipera.notification.domain.model.FileNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final IFileNotificationService fileProcessingService;

    public NotificationController(IFileNotificationService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @PostMapping("/file-notification")
    public HttpStatus handleNotification(@RequestBody FileNotification notification) {
        String filePath = notification.getFilePath();
        fileProcessingService.processFile(filePath);
        return HttpStatus.OK;
    }


}
