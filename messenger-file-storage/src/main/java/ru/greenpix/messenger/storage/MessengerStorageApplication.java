package ru.greenpix.messenger.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MessengerStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerStorageApplication.class, args);
    }
}
