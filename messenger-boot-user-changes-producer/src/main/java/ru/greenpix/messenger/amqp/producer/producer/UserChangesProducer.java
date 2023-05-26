package ru.greenpix.messenger.amqp.producer.producer;

import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;

public interface UserChangesProducer {

    void sendChanges(UserChangesAmqpDto dto);

}
