package ru.greenpix.messenger.chat.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.dto.ModificationChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.GroupChat;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper {

    ChatDto toDto(Chat chat);

    ChatDetailsDto toDetailsDto(GroupChat chat);

    // TODO @Mapping(source = "members", target = "memberIds")
    GroupChat toGroupChatEntity(ModificationChatDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupChat partialUpdateGroupChatEntity(ModificationChatDto dto, @MappingTarget GroupChat entity);

}