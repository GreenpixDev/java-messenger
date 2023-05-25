package ru.greenpix.messenger.notification.mapper.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.greenpix.messenger.common.specification.BaseSpecification;
import ru.greenpix.messenger.common.util.SpecificationBuilder;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.entity.Notification_;
import ru.greenpix.messenger.notification.mapper.FilterMapper;

@Component
public class FilterMapperImpl implements FilterMapper {

    @Override
    public @NotNull Specification<Notification> toNotificationSpecification(NotificationFilterListDto dto) {
        return new SpecificationBuilder<Notification>()
                .addGeneric(Notification_.type, dto.getFilterType(), BaseSpecification::in)
                .addGeneric(Notification_.deliveryTimestamp, dto.getFilterTimestampFrom(), dto.getFilterTimestampTo(), BaseSpecification::betweenData)
                .containsIgnoreCase(Notification_.text, dto.getFilterText())
                .build();
    }
}
