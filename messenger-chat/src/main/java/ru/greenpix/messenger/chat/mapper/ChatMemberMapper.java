package ru.greenpix.messenger.chat.mapper;

import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;

public interface ChatMemberMapper {

    ChatMember toChatMember(Chat chat, UserIntegrationDto dto);

}