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

/**
 * Вспомогательный класс по паттерну Builder для построения {@link Specification}
 * @param <E> тип сущности для спецификации
 */
public class SpecificationBuilder<E> {

    private final List<Specification<E>> specifications = new ArrayList<>();

    /**
     * Добавить спецификацию оператора сравнения equal
     * @param attribute аттрибут, который надо сравнить
     * @param value значение, с которым надо сравнить
     * @return this
     */
    @NotNull
    public SpecificationBuilder<E> equal(
            @NotNull SingularAttribute<E, ?> attribute,
            @Nullable Object value
    ) {
        return add(attribute, value, BaseSpecification::equal);
    }

    /**
     * Добавить спецификацию содержания подстроки с игнорированием регистра
     * @param attribute аттрибут, по которому будет выполняться поиск
     * @param value подстрока, по которой надо выполнить поиск
     * @return this
     */
    @NotNull
    public SpecificationBuilder<E> containsIgnoreCase(
            @NotNull SingularAttribute<E, String> attribute,
            @Nullable String value
    ) {
        return addGeneric(attribute, value, BaseSpecification::containsIgnoreCase);
    }

    /**
     * Добавить спецификацию (generic версия)
     * @param attribute аттрибут
     * @param value значение
     * @param specGetter функция создания спецификации на основе атрибута и значения
     * @return this
     */
    @NotNull
    public <T, V> SpecificationBuilder<E> addGeneric(
            @NotNull SingularAttribute<E, T> attribute,
            @Nullable V value,
            @NotNull BiFunction<SingularAttribute<E, T>, V, Specification<E>> specGetter
    ) {
        if (value != null) {
            specifications.add(specGetter.apply(attribute, value));
        }
        return this;
    }

    /**
     * Добавить спецификацию
     * @param attribute аттрибут
     * @param value значение
     * @param specGetter функция создания спецификации на основе атрибута и значения
     * @return this
     */
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

    /**
     * Метод построения спецификации
     * @return Specification
     */
    @NotNull
    public Specification<E> build() {
        return Objects.requireNonNull(BaseSpecification.all(specifications));
    }
}
