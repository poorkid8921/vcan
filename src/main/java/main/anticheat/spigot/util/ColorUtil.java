package main.anticheat.spigot.util;

import main.anticheat.spigot.config.Config;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {
    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String translate(String message) {
        message = message.replaceAll("%prefix%", Config.PREFIX);
        if (ServerUtil.isHigherThan1_16()) {
            final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            for (Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
                final String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, ChatColor.of(color) + "");
            }
            message = message.replace('&', '§');
            return message;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
