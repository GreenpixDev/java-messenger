package ru.greenpix.messenger.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.greenpix.messenger.notification.dto.NotificationDto;
import ru.greenpix.messenger.notification.entity.Notification;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(source = "deliveryTimestamp", target = "receiveTimestamp")
    NotificationDto toDto(Notification entity);

}