package ru.greenpix.messenger.common.mapper;

import org.springframework.data.domain.Page;
import ru.greenpix.messenger.common.dto.PageDto;

public interface PageMapper {

    <T> PageDto<T> toDto(Page<T> page);

}
