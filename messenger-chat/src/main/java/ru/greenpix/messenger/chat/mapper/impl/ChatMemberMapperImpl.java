package ru.greenpix.messenger.chat.mapper.impl;

import org.springframework.stereotype.Component;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.ChatMember;
import ru.greenpix.messenger.chat.entity.ChatMemberId;
import ru.greenpix.messenger.chat.mapper.ChatMemberMapper;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;

@Component
public class ChatMemberMapperImpl implements ChatMemberMapper {

    @Override
    public ChatMember toChatMember(Chat chat, UserIntegrationDto dto) {
        ChatMember chatMember = new ChatMember();
        chatMember.setId(new ChatMemberId(chat, dto.getId()));
        chatMember.setMemberName(dto.getFullName());
        chatMember.setMemberAvatarId(dto.getAvatarId());
        return chatMember;
    }

}
