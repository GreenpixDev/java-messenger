package ru.greenpix.messenger.storage.util;

import java.io.IOException;

/**
 * Вспомогательный класс.
 * Представляет функцию, которая принимает один аргумент и возвращает результат.
 * Такая функция может выбрасывать исключение
 * @param <T> тип входа в функцию
 * @param <R> тип результата функции
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws IOException;

}
