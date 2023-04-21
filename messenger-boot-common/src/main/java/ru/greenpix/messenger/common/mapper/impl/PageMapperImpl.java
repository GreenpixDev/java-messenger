package ru.greenpix.messenger.common.mapper.impl;

import org.springframework.data.domain.Page;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;

public class PageMapperImpl implements PageMapper {

    @Override
    public <T> PageDto<T> toDto(Page<T> page) {
        return new PageDto<>(
                page.getNumber() + 1,
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.toList()
        );
    }
}
