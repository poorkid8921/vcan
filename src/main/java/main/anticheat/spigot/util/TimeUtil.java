package main.anticheat.spigot.util;

import main.anticheat.spigot.Anticheat;

public final class TimeUtil {
    private TimeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean elapsed(final int from, final int required) {
        return Anticheat.INSTANCE.getTickManager().getTicks() - from > required;
    }

    public static boolean elapsed(final long from, final long required) {
        return System.currentTimeMillis() - from > required;
    }
}
