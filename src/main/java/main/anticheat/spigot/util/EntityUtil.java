package main.anticheat.spigot.util;

import org.bukkit.entity.Entity;

public final class EntityUtil {
    private EntityUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isShulker(final Entity entity) {
        return entity.getType().toString().equals("SHULKER");
    }

    public static boolean isBoat(final Entity entity) {
        return entity.getType().toString().contains("BOAT");
    }
}
