package ru.greenpix.messenger.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MessengerChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerChatApplication.class, args);
    }

}
