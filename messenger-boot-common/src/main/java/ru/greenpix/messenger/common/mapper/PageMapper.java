package ru.greenpix.messenger.common.mapper;

import org.springframework.data.domain.Page;
import ru.greenpix.messenger.common.dto.PageDto;

public interface PageMapper {

    /**
     * Метод конвертации {@link Page} в {@link PageDto}
     * @param page страница из spring'а
     * @return конвертированная DTO страницы
     * @param <T> тип элементов страницы
     */
    <T> PageDto<T> toDto(Page<T> page);

}
