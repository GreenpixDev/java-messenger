package ru.greenpix.messenger.chat.mapper.impl;

import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.GroupChat;
import ru.greenpix.messenger.chat.mapper.ChatMapper;

// TODO
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatDto toDto(Chat chat) {
        return null;
    }

    @Override
    public ChatDetailsDto toDetailsDto(GroupChat chat) {
        return null;
    }
}
