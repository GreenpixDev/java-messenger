package ru.greenpix.messenger.friends.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;
import ru.greenpix.messenger.amqp.service.UserChangesService;
import ru.greenpix.messenger.friends.repository.BlacklistRepository;
import ru.greenpix.messenger.friends.repository.FriendRepository;

@Service
@RequiredArgsConstructor
public class UserChangesServiceImpl implements UserChangesService {

    private final Logger log;
    private final FriendRepository friendRepository;
    private final BlacklistRepository blacklistRepository;

    @Override
    public void update(UserChangesAmqpDto dto) {
        log.trace("Receive AMQP DTO for inner user {}", dto.getId());

        friendRepository.updateFullName(dto.getId(), dto.getFullName());
        blacklistRepository.updateFullName(dto.getId(), dto.getFullName());

        log.debug("Inner user {} has been updated from AMQP", dto.getId());
    }
}
