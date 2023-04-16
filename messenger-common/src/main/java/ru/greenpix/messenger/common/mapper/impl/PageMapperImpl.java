package ru.greenpix.messenger.common.mapper.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.greenpix.messenger.common.dto.PageDto;
import ru.greenpix.messenger.common.mapper.PageMapper;

@Component
public class PageMapperImpl implements PageMapper {

    @Override
    public <T> PageDto<T> toDto(Page<T> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize() + 1,
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.toList()
        );
    }
}
