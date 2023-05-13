package ru.greenpix.messenger.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;
import ru.greenpix.messenger.jwt.model.JwtUser;
import ru.greenpix.messenger.notification.dto.NotificationDto;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.dto.RequestUpdateNotificationStatusDto;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.mapper.NotificationMapper;
import ru.greenpix.messenger.notification.service.NotificationService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Tag(name = "Уведомления")
@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;
    private final PageMapper pageMapper;
    private final NotificationMapper notificationMapper;

    @Operation(
            summary = "Получение списка уведомлений",
            description = "Всегда в порядке убывания по дате получения"
    )
    @GetMapping
    public PageDto<NotificationDto> getChatList(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @Positive
            @RequestParam(name = "page", defaultValue = "1")
            int pageNumber,

            @Positive
            @Max(100)
            @RequestParam(name = "size", defaultValue = "10")
            int pageSize,

            @ParameterObject
            NotificationFilterListDto filters
    ) {
        Page<Notification> page = notificationService.getNotifications(jwtUser.getId(), pageNumber, pageSize, filters);
        return pageMapper.toDto(page.map(notificationMapper::toDto));
    }

    @Operation(
            summary = "Кол-во непрочитанных сообщений",
            description = "Не должны приниматься какие-либо параметры, на выходе метод возвращает только кол-во не прочтённых"
    )
    @GetMapping
    public int getUnreadNotificationsCount(
            @AuthenticationPrincipal
            JwtUser jwtUser
    ) {
        return notificationService.getUnreadNotificationCount(jwtUser.getId());
    }

    @Operation(
            summary = "Пометка уведомлений прочитанными/не прочитанными")
    @PostMapping
    public void updateNotificationStatus(
            @AuthenticationPrincipal
            JwtUser jwtUser,

            @RequestBody
            @Valid
            RequestUpdateNotificationStatusDto dto
    ) {
        throw new UnsupportedOperationException("Not implemented"); // TODO
    }
}
