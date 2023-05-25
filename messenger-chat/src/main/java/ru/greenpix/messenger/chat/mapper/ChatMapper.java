package ru.greenpix.messenger.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.chat.dto.ChatDetailsDto;
import ru.greenpix.messenger.chat.dto.ChatDto;
import ru.greenpix.messenger.chat.entity.Chat;
import ru.greenpix.messenger.chat.entity.GroupChat;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper {

    ChatDto toDto(Chat chat);

    ChatDetailsDto toDetailsDto(GroupChat chat);

    // TODO убрать
 /*   @Deprecated
    GroupChat toGroupChatEntity(ModificationChatDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupChat partialUpdateGroupChatEntity(ModificationChatDto dto, @MappingTarget GroupChat entity);*/

}