package ru.greenpix.messenger.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательный класс по паттерну Builder для построения {@link Sort}
 */
public class SortBuilder {

    private final List<Sort.Order> orders = new ArrayList<>();

    /**
     * Добавить критерий сортировки
     * @param name название критерия
     * @param direction направленность сортировки
     * @return this
     */
    @NotNull
    public SortBuilder add(@NotNull String name, @Nullable Sort.Direction direction) {
        if (direction != null) {
            orders.add(new Sort.Order(direction, name));
        }
        return this;
    }

    /**
     * Построить сортировку
     * @return Sort
     */
    @NotNull
    public Sort build() {
        return Sort.by(orders);
    }

}
