package ru.greenpix.messenger.user.producer;

import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;

public interface UserChangesProducer {

    void sendChanges(UserChangesAmqpDto dto);

}
