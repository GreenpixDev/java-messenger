package ru.greenpix.messenger.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {"ru.greenpix.messenger.common", "ru.greenpix.messenger.user"})
public class MessengerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerUserApplication.class, args);
    }

}
