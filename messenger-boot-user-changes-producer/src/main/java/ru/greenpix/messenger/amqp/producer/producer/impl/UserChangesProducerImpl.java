package ru.greenpix.messenger.amqp.producer.producer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;
import ru.greenpix.messenger.amqp.producer.producer.UserChangesProducer;

@RequiredArgsConstructor
public class UserChangesProducerImpl implements UserChangesProducer {

    private final StreamBridge streamBridge;
    private final String bindingName;

    @Override
    public void sendChanges(UserChangesAmqpDto dto) {
        streamBridge.send(bindingName, dto);
    }
}
