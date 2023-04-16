package ru.greenpix.messenger.friends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MessengerFriendsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerFriendsApplication.class, args);
    }

}
