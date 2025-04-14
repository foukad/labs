package fr.ttis.npp.ipera.notification.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    final
    NotificationPropertiesConfiguration notificationPropertiesConfiguration;

    public RestTemplateConfiguration(NotificationPropertiesConfiguration notificationPropertiesConfiguration) {
        this.notificationPropertiesConfiguration = notificationPropertiesConfiguration;
    }

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
