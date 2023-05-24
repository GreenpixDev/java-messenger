package ru.greenpix.messenger.storage.util;

import java.io.IOException;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws IOException;

}
