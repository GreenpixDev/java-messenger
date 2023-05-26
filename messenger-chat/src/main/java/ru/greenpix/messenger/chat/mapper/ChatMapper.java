package ru.greenpix.messenger.chat.mapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.Message;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper {

    @Mapping(source = "chat.id", target = "id")
    @Mapping(source = "chatName", target = "name")
    @Mapping(source = "lastMessage.text", target = "lastMessageText")
    @Mapping(source = "lastMessage.senderId", target = "lastMessageSenderId")
    @Mapping(source = "lastMessage.creationTimestamp", target = "lastMessageSendingTimestamp")
    @NotNull
    ChatDto toDto(
            @NotNull Chat chat,
            @NotNull String chatName,
            @NotNull Message lastMessage
    );

    @Mapping(source = "chatName", target = "name")
    @Mapping(source = "chatAvatarId", target = "avatarId")
    @Mapping(source = "chatAdminId", target = "adminUserId")
    @Mapping(source = "chat.creationTimestamp", target = "creationTimestamp")
    @NotNull
    ChatDetailsDto toDetailsDto(
            @NotNull Chat chat,
            @NotNull String chatName,
            @Nullable UUID chatAvatarId,
            @Nullable UUID chatAdminId
    );

}