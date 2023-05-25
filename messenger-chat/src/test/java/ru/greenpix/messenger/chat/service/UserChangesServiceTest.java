package ru.greenpix.messenger.chat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import ru.greenpix.messenger.amqp.dto.UserChangesAmqpDto;
import ru.greenpix.messenger.chat.repository.ChatMemberRepository;
import ru.greenpix.messenger.chat.service.impl.UserChangesServiceImpl;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class UserChangesServiceTest {

    /*
     * Тестовые данные
     */

    static UUID ID_TEST = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");
    static String STRING_TEST = "Test";
    static UserChangesAmqpDto DTO_TEST = new UserChangesAmqpDto(ID_TEST, STRING_TEST, ID_TEST);

    /*
     * Заглушки
     */

    @Mock
    Logger log;
    @Mock
    ChatMemberRepository chatRepository;

    /*
     * Тестируемый объект
     */

    @InjectMocks
    UserChangesServiceImpl userChangesService;

    /*
     * Тесты
     */

    @DisplayName("Проверка обновления данных пользователей в БД")
    @Test
    void updateTest() {
        userChangesService.update(DTO_TEST);
        verify(chatRepository, times(1))
                .updateNameAndAvatarId(eq(ID_TEST), eq(STRING_TEST), eq(ID_TEST));
    }
}
