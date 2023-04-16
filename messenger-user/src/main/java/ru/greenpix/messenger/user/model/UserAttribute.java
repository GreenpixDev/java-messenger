package ru.greenpix.messenger.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.greenpix.messenger.user.entity.User;
import ru.greenpix.messenger.user.entity.User_;

import javax.persistence.metamodel.SingularAttribute;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum UserAttribute {

    ID(User_.id),
    USERNAME(User_.username),
    EMAIL(User_.email),
    FULL_NAME(User_.fullName),
    REGISTRATION_TIMESTAMP(User_.registrationTimestamp),
    BIRTH_DATE(User_.birthDate),
    PHONE(User_.phone),
    CITY(User_.city);

    private final SingularAttribute<User, ?> attribute;

    public static @Nullable UserAttribute getByName(@NotNull String name) {
        return Stream.of(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
