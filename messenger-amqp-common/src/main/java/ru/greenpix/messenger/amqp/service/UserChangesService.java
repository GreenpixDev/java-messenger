package ru.greenpix.messenger.amqp.service;

import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;

public interface UserChangesService {

    // TODO javadoc
    void update(UserChangesAmqpDto dto);

}
