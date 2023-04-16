package ru.greenpix.messenger.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import ru.greenpix.messenger.common.specification.BaseSpecification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class SpecificationBuilder<E> {

    private final List<Specification<E>> specifications = new ArrayList<>();

    @NotNull
    public SpecificationBuilder<E> equal(
            @NotNull SingularAttribute<E, ?> attribute,
            @Nullable Object value
    ) {
        return add(attribute, value, BaseSpecification::equal);
    }

    @NotNull
    public SpecificationBuilder<E> containsIgnoreCase(
            @NotNull SingularAttribute<E, String> attribute,
            @Nullable String value
    ) {
        return addGeneric(attribute, value, BaseSpecification::containsIgnoreCase);
    }

    @NotNull
    public <T> SpecificationBuilder<E> addGeneric(
            @NotNull SingularAttribute<E, T> attribute,
            @Nullable T value,
            @NotNull BiFunction<SingularAttribute<E, T>, T, Specification<E>> specGetter
    ) {
        if (value != null) {
            specifications.add(specGetter.apply(attribute, value));
        }
        return this;
    }

    @NotNull
    public SpecificationBuilder<E> add(
            @NotNull SingularAttribute<E, ?> attribute,
            @Nullable Object value,
            @NotNull BiFunction<SingularAttribute<E, ?>, Object, Specification<E>> specGetter
    ) {
        if (value != null) {
            specifications.add(specGetter.apply(attribute, value));
        }
        return this;
    }

    @NotNull
    public Specification<E> build() {
        return Objects.requireNonNull(BaseSpecification.all(specifications));
    }
}
