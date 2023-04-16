package ru.greenpix.messenger.user.mapper.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.greenpix.messenger.common.util.SortBuilder;
import ru.greenpix.messenger.user.dto.UserSortListDto;
import ru.greenpix.messenger.user.entity.User_;
import ru.greenpix.messenger.user.mapper.SortMapper;

@Component
public class SortMapperImpl implements SortMapper {

    @Override
    public @NotNull Sort toUserSort(UserSortListDto dto) {
        return new SortBuilder()
                .add(User_.ID, dto.getSortId())
                .add(User_.REGISTRATION_TIMESTAMP, dto.getSortRegistrationDate())
                .add(User_.USERNAME, dto.getSortUsername())
                .add(User_.EMAIL, dto.getSortEmail())
                .add(User_.FULL_NAME, dto.getSortFullName())
                .add(User_.BIRTH_DATE, dto.getSortBirthDate())
                .add(User_.PHONE, dto.getSortPhone())
                .add(User_.CITY, dto.getSortCity())
                .add(User_.AVATAR_ID, dto.getSortAvatar())
                .build();
    }
}
