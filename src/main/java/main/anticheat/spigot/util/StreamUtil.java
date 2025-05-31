package main.anticheat.spigot.util;

import java.util.Collection;
import java.util.function.Predicate;

public final class StreamUtil {
    private StreamUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> boolean anyMatch(final Collection<T> collection, final Predicate<T> condition) {
        if (condition == null) {
            return false;
        }
        for (final T object : collection) {
            if (condition.test(object)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean noneMatch(final Collection<T> collection, final Predicate<T> condition) {
        if (condition == null) {
            return true;
        }
        for (final T object : collection) {
            if (condition.test(object)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean allMatch(final Collection<T> collection, final Predicate<T> condition) {
        if (condition == null) {
            return false;
        }
        for (final T object : collection) {
            if (!condition.test(object)) {
                return false;
            }
        }
        return true;
    }
}
