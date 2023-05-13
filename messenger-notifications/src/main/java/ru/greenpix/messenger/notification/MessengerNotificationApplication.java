package ru.greenpix.messenger.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MessengerNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerNotificationApplication.class, args);
    }

}
