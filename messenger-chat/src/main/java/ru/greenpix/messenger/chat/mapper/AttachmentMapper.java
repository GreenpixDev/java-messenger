package ru.greenpix.messenger.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.chat.dto.AttachmentDto;
import ru.greenpix.messenger.chat.entity.Attachment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper {

    @Mapping(source = "fileId", target = "id")
    @Mapping(source = "fileName", target = "name")
    @Mapping(source = "fileSize", target = "size")
    AttachmentDto toDto(Attachment chat);

}