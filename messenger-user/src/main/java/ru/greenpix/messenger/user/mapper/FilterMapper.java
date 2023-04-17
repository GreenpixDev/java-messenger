package ru.greenpix.messenger.user.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.user.dto.UserFilterListDto;
import ru.greenpix.messenger.user.entity.User;

public interface FilterMapper {

    /**
     * Метод конвертации DTO списка фильтров пользователя в объект спецификации spring'а
     * @param dto список параметров сортировки
     * @return конвертированный {@link Specification} сущностей {@link User} для работы с репозиторием
     */
    @NotNull
    Specification<User> toUserSpecification(UserFilterListDto dto);

}
