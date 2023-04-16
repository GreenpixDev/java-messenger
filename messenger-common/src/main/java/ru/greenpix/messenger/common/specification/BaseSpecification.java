package ru.greenpix.messenger.common.specification;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;

public class BaseSpecification {

    @SafeVarargs
    public static @Nullable <E> Specification<E> compose(
            @NotNull BiFunction<Specification<E>, Specification<E>, Specification<E>> composeFunction,
            @NotNull Specification<E>... specifications
    ) {
        if (specifications.length == 0) return null;

        Specification<E> composed = specifications[0];
        for (int i = 1; i < specifications.length; i++) {
            composed = composeFunction.apply(composed, specifications[i]);
        }
        return composed;
    }

    public static @Nullable <E> Specification<E> compose(
            @NotNull BiFunction<Specification<E>, Specification<E>, Specification<E>> composeFunction,
            @NotNull Collection<Specification<E>> specifications
    ) {
        if (specifications.isEmpty()) return null;

        Iterator<Specification<E>> iterator = specifications.iterator();
        Specification<E> composed = iterator.next();
        while (iterator.hasNext()) {
            composed = composeFunction.apply(composed, iterator.next());
        }
        return composed;
    }

    @SafeVarargs
    public static @Nullable <E> Specification<E> all(@NotNull Specification<E>... specifications) {
        return compose(Specification::and, specifications);
    }

    public static @Nullable <E> Specification<E> all(@NotNull Collection<Specification<E>> specifications) {
        return compose(Specification::and, specifications);
    }

    @SafeVarargs
    public static @Nullable <E> Specification<E> any(@NotNull Specification<E>... specifications) {
        return compose(Specification::or, specifications);
    }

    public static @Nullable <E> Specification<E> any(@NotNull Collection<Specification<E>> specifications) {
        return compose(Specification::or, specifications);
    }

    public static @NotNull <E> Specification<E> equal(@NotNull SingularAttribute<E, ?> attribute, @NotNull Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attribute), value);
    }

    public static @NotNull <E> Specification<E> like(@NotNull SingularAttribute<E, String> attribute, @NotNull String expression) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(attribute), expression);
    }

    public static @NotNull <E> Specification<E> contains(@NotNull SingularAttribute<E, String> attribute, @NotNull String substring) {
        return like(attribute, "%" + substring + "%");
    }
}
