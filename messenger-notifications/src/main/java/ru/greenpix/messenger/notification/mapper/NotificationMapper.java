package ru.greenpix.messenger.notification.mapper;

import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.notification.dto.NotificationDto;
import ru.greenpix.messenger.notification.dto.NotificationStatus;
import ru.greenpix.messenger.notification.entity.Notification;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(source = "deliveryTimestamp", target = "receiveTimestamp")
    @Mapping(source = "readingTimestamp", target = "status", qualifiedByName = "readingTimestampToStatus")
    NotificationDto toDto(Notification entity);

    Notification toEntity(NotificationAmqpDto dto);

    @Named("readingTimestampToStatus")
    static NotificationStatus readingTimestampToStatus(@Nullable LocalDateTime readingTimestamp) {
        return readingTimestamp == null ? NotificationStatus.UNREAD : NotificationStatus.READ;
    }
}