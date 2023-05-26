package ru.greenpix.messenger.amqp.producer.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.amqp.producer.producer.NotificationProducer;

@RequiredArgsConstructor
public class NotificationProducerImpl implements NotificationProducer {

    private final StreamBridge streamBridge;
    private final String bindingName;

    @Override
    public void sendNotification(NotificationAmqpDto dto) {
        streamBridge.send(bindingName, dto);
    }
}
