package ru.greenpix.messenger.common.specification;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * Вспомогательный класс для создания основных спецификаций
 * @see Specification
 */
public class BaseSpecification {

    /**
     * Метод составления спецификации из других дочерних спецификаций
     * @param specifications массив других дочерних операторов
     * @param composeFunction функция составления спецификации
     * @return составная {@link Specification}
     * @param <E> тип сущности для спецификации
     */
    @SafeVarargs
    public static @NotNull <E> Specification<E> compose(
            @NotNull BiFunction<Specification<E>, Specification<E>, Specification<E>> composeFunction,
            @NotNull Specification<E>... specifications
    ) {
        if (specifications.length == 0) return Specification.where(null);

        Specification<E> composed = specifications[0];
        for (int i = 1; i < specifications.length; i++) {
            composed = composeFunction.apply(composed, specifications[i]);
        }
        return composed;
    }

    /**
     * Метод составления спецификации из других дочерних спецификаций
     * @param specifications коллекция других дочерних операторов
     * @param composeFunction функция составления спецификации
     * @return составная {@link Specification}
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> compose(
            @NotNull BiFunction<Specification<E>, Specification<E>, Specification<E>> composeFunction,
            @NotNull Collection<Specification<E>> specifications
    ) {
        if (specifications.isEmpty()) return Specification.where(null);

        Iterator<Specification<E>> iterator = specifications.iterator();
        Specification<E> composed = iterator.next();
        while (iterator.hasNext()) {
            composed = composeFunction.apply(composed, iterator.next());
        }
        return composed;
    }

    /**
     * Спецификация оператора AND для массива других дочерних операторов
     * @param specifications массив других дочерних операторов
     * @return Specification, которая будет искать сущности, где все дочерние операторы выполняются
     * @param <E> тип сущности для спецификации
     */
    @SafeVarargs
    public static @NotNull <E> Specification<E> all(@NotNull Specification<E>... specifications) {
        return compose(Specification::and, specifications);
    }

    /**
     * Спецификация оператора AND для коллекции других дочерних операторов
     * @param specifications коллекция других дочерних операторов
     * @return Specification, которая будет искать сущности, где все дочерние операторы выполняются
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> all(@NotNull Collection<Specification<E>> specifications) {
        return compose(Specification::and, specifications);
    }

    /**
     * Спецификация оператора OR для массива других дочерних операторов
     * @param specifications массив других дочерних операторов
     * @return Specification, которая будет искать сущности, где хотя бы один дочерни1 оператор выполняется
     * @param <E> тип сущности для спецификации
     */
    @SafeVarargs
    public static @NotNull <E> Specification<E> any(@NotNull Specification<E>... specifications) {
        return compose(Specification::or, specifications);
    }

    /**
     * Спецификация оператора OR для коллекции других дочерних операторов
     * @param specifications коллекция других дочерних операторов
     * @return Specification, которая будет искать сущности, где хотя бы один дочерни1 оператор выполняется
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> any(@NotNull Collection<Specification<E>> specifications) {
        return compose(Specification::or, specifications);
    }

    /**
     * Спецификация оператора сравнения EQUAL
     * @param attribute аттрибут сущности, который надо сравнить
     * @param value значение, с которым надо сравнить
     * @return Specification, которая будет искать сущности, у которых `attribute = value`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> equal(@Nullable SingularAttribute<E, ?> attribute, @NotNull Object value) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attribute), value);
    }

    /**
     * Спецификация оператора IN
     * @param attribute аттрибут сущности, который должен быть включен в множество значений
     * @param values множество значений
     * @return Specification, которая будет искать сущности, у которых `attribute in value`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> in(@Nullable SingularAttribute<E, ?> attribute, @NotNull Collection<?> values) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> root.get(attribute).in(values);
    }

    /**
     * Спецификация оператора LIKE
     * @param attribute аттрибут сущности
     * @param expression like выражения
     * @return Specification, которая будет искать сущности, где `attribute LIKE value`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> like(@Nullable SingularAttribute<E, String> attribute, @NotNull String expression) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(attribute), expression);
    }

    /**
     * Спецификация оператора LIKE с игнорированием регистра
     * @param attribute аттрибут сущности
     * @param expression like выражения
     * @return Specification, которая будет искать сущности, где `upper(attribute) LIKE upper(value)`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> likeIgnoreCase(@Nullable SingularAttribute<E, String> attribute, @NotNull String expression) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.upper(root.get(attribute)),
                expression.toUpperCase()
        );
    }

    /**
     * Спецификация поиска по содержанию подстроки
     * @param attribute аттрибут сущности
     * @param substring подстрока
     * @return Specification, которая будет искать сущности, где `attribute LIKE '%' + value + '%'`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> contains(@Nullable SingularAttribute<E, String> attribute, @NotNull String substring) {
        if (attribute == null) return empty();
        return like(attribute, "%" + substring + "%");
    }

    /**
     * Спецификация поиска по содержанию подстроки с игнорированием регистра
     * @param attribute аттрибут сущности
     * @param substring подстрока
     * @return Specification, которая будет искать сущности, где `upper(attribute) LIKE upper('%' + value + '%')`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> containsIgnoreCase(@Nullable SingularAttribute<E, String> attribute, @NotNull String substring) {
        if (attribute == null) return empty();
        return likeIgnoreCase(attribute, "%" + substring + "%");
    }

    /**
     * Спецификация оператора сравнения EQUAL с проверкой даты
     * @param attribute аттрибут сущности, который надо сравнить
     * @param value дата, с которой надо сравнить
     * @return Specification, которая будет искать сущности, у которых `DATE(attribute) = value`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> equalDate(@Nullable SingularAttribute<E, LocalDateTime> attribute, @NotNull LocalDate value) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.function("DATE", LocalDate.class, root.get(attribute)),
                value
        );
    }

    /**
     * Спецификация оператора сравнения BETWEEN
     * @param attribute аттрибут сущности, который надо сравнить
     * @param from левая граница
     * @param to правая граница
     * @param <E> тип сущности для спецификации
     * @param <Y> тип, который сравнивается
     * @return Specification, которая будет искать сущности, у которых `attribute BETWEEN from TO to`
     */
    public static @NotNull <E, Y extends Comparable<? super Y>> Specification<E> between(
            @Nullable SingularAttribute<E, Y> attribute,
            @NotNull Y from,
            @NotNull Y to
    ) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(attribute), from, to);
    }

    /**
     * Спецификация оператора сравнения BETWEEN для дат
     * @param attribute аттрибут сущности, который надо сравнить
     * @param from левая граница (дата)
     * @param to правая граница (дата)
     * @param <E> тип сущности для спецификации
     * @return Specification, которая будет искать сущности, у которых `attribute BETWEEN from TO to`
     */
    public static @NotNull <E> Specification<E> betweenData(
            @Nullable SingularAttribute<E, LocalDateTime> attribute,
            @NotNull LocalDate from,
            @NotNull LocalDate to
    ) {
        if (attribute == null) return empty();
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                criteriaBuilder.function("DATE", LocalDate.class, root.get(attribute)),
                from, to
        );
    }

    public static @NotNull <E> Specification<E> empty() {
        return (root, query, criteriaBuilder) -> null;
    }
}
