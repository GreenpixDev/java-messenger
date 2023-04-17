package ru.greenpix.messenger.common.specification;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
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
    public static @NotNull <E> Specification<E> equal(@NotNull SingularAttribute<E, ?> attribute, @NotNull Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attribute), value);
    }

    /**
     * Спецификация оператора LIKE
     * @param attribute аттрибут сущности
     * @param expression like выражения
     * @return Specification, которая будет искать сущности, где `attribute LIKE value`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> like(@NotNull SingularAttribute<E, String> attribute, @NotNull String expression) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(attribute), expression);
    }

    /**
     * Спецификация оператора LIKE с игнорированием регистра
     * @param attribute аттрибут сущности
     * @param expression like выражения
     * @return Specification, которая будет искать сущности, где `upper(attribute) LIKE upper(value)`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> likeIgnoreCase(@NotNull SingularAttribute<E, String> attribute, @NotNull String expression) {
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
    public static @NotNull <E> Specification<E> contains(@NotNull SingularAttribute<E, String> attribute, @NotNull String substring) {
        return like(attribute, "%" + substring + "%");
    }

    /**
     * Спецификация поиска по содержанию подстроки с игнорированием регистра
     * @param attribute аттрибут сущности
     * @param substring подстрока
     * @return Specification, которая будет искать сущности, где `upper(attribute) LIKE upper('%' + value + '%')`
     * @param <E> тип сущности для спецификации
     */
    public static @NotNull <E> Specification<E> containsIgnoreCase(@NotNull SingularAttribute<E, String> attribute, @NotNull String substring) {
        return likeIgnoreCase(attribute, "%" + substring + "%");
    }
}
