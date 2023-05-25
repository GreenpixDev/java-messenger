package ru.greenpix.messenger.amqp.service;

import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;

public interface UserChangesService {

    /**
     * Метод обновления данных о пользователе внутри других микросервисов.
     * Данный метод будет вызываться при получении сообщений из AMQP с сервиса "Пользователи".
     * @param dto DTO сообщения об изменении данных пользователя в микросервисе "Пользователи"
     */
    void update(UserChangesAmqpDto dto);

}
