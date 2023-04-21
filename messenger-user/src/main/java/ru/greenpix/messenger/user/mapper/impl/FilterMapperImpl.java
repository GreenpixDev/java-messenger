package ru.greenpix.messenger.user.mapper.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.greenpix.messenger.common.specification.BaseSpecification;
import ru.greenpix.messenger.common.util.SpecificationBuilder;
import ru.greenpix.messenger.user.dto.UserFilterListDto;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.entity.User_;
import ru.greenpix.messenger.user.mapper.FilterMapper;

@Component
public class FilterMapperImpl implements FilterMapper {

    @Override
    public @NotNull Specification<User> toUserSpecification(UserFilterListDto dto) {
        return new SpecificationBuilder<User>()
                .addGeneric(User_.registrationTimestamp, dto.getFilterRegistrationDate(), BaseSpecification::equalDate)
                .containsIgnoreCase(User_.username, dto.getFilterUsername())
                .containsIgnoreCase(User_.email, dto.getFilterEmail())
                .containsIgnoreCase(User_.fullName, dto.getFilterFullName())
                .equal(User_.birthDate, dto.getFilterBirthDate())
                .containsIgnoreCase(User_.phone, dto.getFilterPhone())
                .containsIgnoreCase(User_.city, dto.getFilterCity())
                .build();
    }
}
