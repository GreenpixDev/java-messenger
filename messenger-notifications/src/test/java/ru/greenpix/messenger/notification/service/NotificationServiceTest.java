package ru.greenpix.messenger.notification.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.amqp.dto.NotificationAmqpDto;
import ru.greenpix.messenger.common.specification.BaseSpecification;
import ru.greenpix.messenger.notification.dto.NotificationFilterListDto;
import ru.greenpix.messenger.notification.dto.NotificationStatus;
import ru.greenpix.messenger.notification.entity.Notification;
import ru.greenpix.messenger.notification.mapper.FilterMapper;
import ru.greenpix.messenger.notification.mapper.NotificationMapper;
import ru.greenpix.messenger.notification.repository.NotificationRepository;
import ru.greenpix.messenger.notification.service.impl.NotificationServiceImpl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    /*
     * Тестовые данные
     */

    static UUID ID_TEST = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");
    static NotificationFilterListDto FILTER_DTO_TEST = new NotificationFilterListDto(
            null, null, null, null
    );
    static NotificationAmqpDto AMQP_DTO_TEST = new NotificationAmqpDto(
            null, null, null
    );
    static Notification ENTITY_TEST = new Notification();
    static Page<Notification> PAGE_TEST = Page.empty();
    static Specification<Notification> SPEC_TEST = BaseSpecification.empty();
    static Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.EPOCH.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );

    /*
     * Заглушки
     */

    @Mock
    NotificationRepository notificationRepository;
    @Mock
    NotificationMapper notificationMapper;
    @Mock
    FilterMapper filterMapper;
    @Mock
    Logger logger;
    @Mock
    Clock clock;

    /*
     * Тестируемый объект
     */

    @InjectMocks
    NotificationServiceImpl notificationService;

    /*
     * Тесты
     */

    @DisplayName("Проверка получения уведомлений")
    @Test
    void getNotificationsTest() {
        when(filterMapper.toNotificationSpecification(any())).thenReturn(SPEC_TEST);
        when(notificationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(PAGE_TEST);

        var page = notificationService.getNotifications(ID_TEST, 1, 1, FILTER_DTO_TEST);
        assertEquals(PAGE_TEST, page);
    }

    @DisplayName("Проверка получения количества непрочитанных уведомлений")
    @Test
    void getUnreadNotificationCountTest() {
        when(notificationRepository.countUnreadByUserId(eq(ID_TEST))).thenReturn(255);

        var count = notificationService.getUnreadNotificationCount(ID_TEST);
        assertEquals(255, count);
    }

    @DisplayName("Проверка обновления статуса на 'прочитано' у уведомлений")
    @Test
    void updateNotificationStatusReadTest() {
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());
        assertDoesNotThrow(() -> notificationService.updateNotificationStatus(
                ID_TEST,
                Collections.emptyList(),
                NotificationStatus.READ
        ));
        verify(notificationRepository, times(1))
                .updateAllReadingTimestampByIds(eq(ID_TEST), any(), eq(Collections.emptyList()));
    }

    @DisplayName("Проверка обновления статуса на 'не прочитано' у уведомлений")
    @Test
    void updateNotificationStatusUnreadTest() {
        assertDoesNotThrow(() -> notificationService.updateNotificationStatus(
                ID_TEST,
                Collections.emptyList(),
                NotificationStatus.UNREAD
        ));
        verify(notificationRepository, times(1))
                .updateAllReadingTimestampByIds(eq(ID_TEST), isNull(), eq(Collections.emptyList()));
    }

    @DisplayName("Проверка сохранения уведомления в БД")
    @Test
    void saveNotificationTest() {
        when(notificationMapper.toEntity(eq(AMQP_DTO_TEST))).thenReturn(ENTITY_TEST);
        when(notificationRepository.save(eq(ENTITY_TEST))).thenReturn(ENTITY_TEST);

        assertDoesNotThrow(() -> notificationService.saveNotification(AMQP_DTO_TEST));

        verify(notificationRepository, times(1))
                .save(eq(ENTITY_TEST));
    }
}
