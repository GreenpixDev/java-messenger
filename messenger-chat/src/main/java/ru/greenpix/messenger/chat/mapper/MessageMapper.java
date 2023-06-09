package ru.greenpix.messenger.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.chat.dto.MessageDetailsDto;
import ru.greenpix.messenger.chat.dto.MessageDto;
import ru.greenpix.messenger.chat.dto.SendingMessageDto;
import ru.greenpix.messenger.chat.entity.Message;
import ru.greenpix.messenger.common.dto.integration.UserIntegrationDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = AttachmentMapper.class)
public interface MessageMapper {

    @Mapping(source = "message.creationTimestamp", target = "sendingTimestamp")
    @Mapping(source = "message.chat.id", target = "chatId")
    @Mapping(source = "chatName", target = "chatName")
    @Mapping(source = "attachmentName", target = "attachmentName")
    @Mapping(source = "attachmentName", target = "hasAttachments", qualifiedByName = "hasAttachments")
    MessageDto toDto(Message message, String chatName, String attachmentName);

    @Mapping(source = "message.id", target = "id")
    @Mapping(source = "message.creationTimestamp", target = "sendingTimestamp")
    @Mapping(source = "message.text", target = "text")
    @Mapping(source = "message.attachments", target = "attachments")
    @Mapping(source = "user.fullName", target = "senderName")
    @Mapping(source = "user.avatarId", target = "senderAvatarId")
    MessageDetailsDto toDetailsDto(Message message, UserIntegrationDto user);

    Message toMessageEntity(SendingMessageDto dto);

    @Named("hasAttachments")
    static boolean hasAttachments(String attachmentName) {
        return attachmentName != null;
    }

}