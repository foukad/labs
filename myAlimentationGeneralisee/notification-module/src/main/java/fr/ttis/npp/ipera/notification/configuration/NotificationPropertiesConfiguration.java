package fr.ttis.npp.ipera.notification.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="ipera")
public class NotificationPropertiesConfiguration {

    @Value("${ipera.event-service.url}")
    String baseUri;
}
