package ru.greenpix.messenger.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;
import ru.greenpix.messenger.amqp.service.UserChangesService;
import ru.greenpix.messenger.chat.repository.ChatMemberRepository;

@Service
@RequiredArgsConstructor
public class UserChangesServiceImpl implements UserChangesService {

    private final Logger log;
    private final ChatMemberRepository chatRepository;

    @Override
    public void update(UserChangesAmqpDto dto) {
        log.trace("Receive AMQP DTO for inner user {}", dto.getId());

        chatRepository.updateNameAndAvatarId(dto.getId(), dto.getFullName(), dto.getAvatarId());

        log.debug("Inner user {} has been updated from AMQP", dto.getId());
    }
}
