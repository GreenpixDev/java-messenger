package ru.greenpix.messenger.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortBuilder {

    private final List<Sort.Order> orders = new ArrayList<>();

    @NotNull
    public SortBuilder add(@NotNull String name, @Nullable Sort.Direction direction) {
        if (direction != null) {
            orders.add(new Sort.Order(direction, name));
        }
        return this;
    }

    @NotNull
    public Sort build() {
        return Sort.by(orders);
    }

}
