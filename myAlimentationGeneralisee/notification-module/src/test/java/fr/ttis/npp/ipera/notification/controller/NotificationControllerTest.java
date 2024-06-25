package fr.ttis.npp.ipera.notification.controller;

import fr.ttis.npp.ipera.notification.application.service.IFileNotificationService;
import fr.ttis.npp.ipera.notification.domain.model.FileNotification;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
    public class NotificationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private IFileNotificationService fileNotificationService;

        @Test
        public void handleNotificationTest() throws Exception {
            val notification = new FileNotification();
            notification.setFilePath("test/path");

            doNothing().when(fileNotificationService).processFile(any(String.class));


       //     RequestEntity<String> body = new RequestEntity<>(new String[]{"\"filePath\":\"test/path\"}"),"post","");
        /*    mockMvc.perform(post("/file-notification")
                            .contentType(MediaType.APPLICATION_JSON)*/
                  //  .body()
                   // .andExpect(status().isOk());
        }
    }