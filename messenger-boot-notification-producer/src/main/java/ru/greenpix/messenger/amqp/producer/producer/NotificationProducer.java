package ru.greenpix.messenger.amqp.producer.producer;

import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;

public interface NotificationProducer {

    void sendNotification(NotificationAmqpDto dto);

}
