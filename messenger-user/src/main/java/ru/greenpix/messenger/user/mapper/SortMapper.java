package ru.greenpix.messenger.user.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import ru.greenpix.messenger.user.dto.UserSortListDto;

public interface SortMapper {

    @NotNull
    Sort toUserSort(UserSortListDto dto);

}
