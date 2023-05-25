package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;
import ru.greenpix.messenger.amqp.service.UserChangesService;

@Service
@RequiredArgsConstructor
public class UserChangesServiceImpl implements UserChangesService {
    @Override
    public void update(UserChangesAmqpDto dto) {
        // TODO
    }
}
