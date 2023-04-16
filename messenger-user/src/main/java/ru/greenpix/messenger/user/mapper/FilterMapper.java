package ru.greenpix.messenger.user.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.user.dto.UserFilterListDto;
import ru.greenpix.messenger.user.entity.User;

public interface FilterMapper {

    @NotNull
    Specification<User> toUserSpecification(UserFilterListDto dto);

}
