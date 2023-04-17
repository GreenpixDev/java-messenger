package ru.greenpix.messenger.user.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import ru.greenpix.messenger.user.dto.UserSortListDto;

public interface SortMapper {

    /**
     * Метод конвертации DTO списка параметров сортировки пользователя в объект сортировки spring'а
     * @param dto список параметров сортировки
     * @return конвертированный {@link Sort} для работы с репозиторием
     */
    @NotNull
    Sort toUserSort(UserSortListDto dto);

}
