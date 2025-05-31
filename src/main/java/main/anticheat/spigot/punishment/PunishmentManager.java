package main.anticheat.spigot.punishment;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import main.anticheat.api.event.VulcanPunishEvent;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PunishmentManager {
    public static String spigot() {
        return "%%__USER__%%";
    }

    public void handlePunishment(final AbstractCheck check, final PlayerData data) {
        if (!Config.PUNISHABLE.get(check.getClassName())) {
            return;
        }
        if (System.currentTimeMillis() - data.getLastPunishment() < Config.PUNISHMENT_DELAY) {
            return;
        }
        data.setLastPunishment(System.currentTimeMillis());
        if (data.getPlayer().hasPermission("vulcan.bypass." + check.getClassName().toLowerCase())) {
            return;
        }
        if (Config.ENABLE_API) {
            final VulcanPunishEvent event = new VulcanPunishEvent(data.getPlayer(), check);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        final int punishmentId = ThreadLocalRandom.current().nextInt(50000);
        if (Config.PUNISHMENT_FILE_ENABLED) {
            LogUtil.logPunishment(check, data);
        }
        if (!Config.PUNISHMENT_MESSAGE.equalsIgnoreCase("")) {
            Anticheat.INSTANCE.getAlertManager().sendMessage(Config.PUNISHMENT_MESSAGE.replaceAll("%prefix%", Config.PREFIX).replaceAll("%server-name%", Config.SERVER_NAME).replaceAll("%punishment-id", Integer.toString(punishmentId)).replaceAll("%player%", data.getPlayer().getName()).replaceAll("%uuid%", data.getPlayer().getUniqueId().toString()).replaceAll("%category%", StringUtils.capitalize(check.getCategory().toLowerCase())).replaceAll("%complex-type%", check.getCheckInfo().complexType()).replaceAll("%description%", check.getCheckInfo().description()).replaceAll("%opponent%", (data.getCombatProcessor().getTrackedPlayer() == null) ? "None" : data.getCombatProcessor().getTrackedPlayer().getName()).replaceAll("%check%", check.getCheckInfo().name()).replaceAll("%client-version%", PlayerUtil.getClientVersionToString(data.getPlayer())).replaceAll("%client-brand%", data.getClientBrand()).replaceAll("%type%", Character.toString(check.getCheckInfo().type())).replaceAll("%vl%", Integer.toString(check.getVl())));
        }
        final boolean broadcastPunishment = Config.BROADCAST_PUNISHMENT.get(check.getClassName());
        if (broadcastPunishment) {
            Config.PUNISHMENT_BROADCAST.forEach(message -> ServerUtil.broadcast(message.replaceAll("%player%", data.getPlayer().getName()).replaceAll("%client-version%", PlayerUtil.getClientVersionToString(data.getPlayer())).replaceAll("%client-brand%", data.getClientBrand()).replaceAll("%check%", check.getCheckInfo().name()).replaceAll("%server-name%", Config.SERVER_NAME).replaceAll("%uuid%", data.getPlayer().getUniqueId().toString()).replaceAll("%opponent%", (data.getCombatProcessor().getTrackedPlayer() == null) ? "None" : data.getCombatProcessor().getTrackedPlayer().getName()).replaceAll("%complex-type%", check.getCheckInfo().complexType()).replaceAll("%category%", StringUtils.capitalize(check.getCategory().toLowerCase())).replaceAll("%punishment-id", Integer.toString(punishmentId)).replaceAll("%description%", check.getCheckInfo().description()).replaceAll("%type%", Character.toString(check.getCheckInfo().type()))));
        }
        final List<String> punishmentCommands = Config.PUNISHMENT_COMMANDS.get(check.getClassName());
        if (!punishmentCommands.isEmpty()) {
            new BukkitRunnable() {
                public void run() {
                    punishmentCommands.forEach(command -> {
                        if (command.startsWith("Bungee:") || command.startsWith("bungee:")) {
                            PunishmentManager.this.sendPluginMessage(data.getPlayer(), ColorUtil.translate(command.replaceAll("Bungee:", "").replaceAll("bungee:", "").replaceAll("%server-name%", Config.SERVER_NAME).replaceAll("%ping%", Integer.toString(data.getConnectionProcessor().getKeepAlivePing())).replaceAll("%tps%", MathUtil.trim(ServerUtil.getTPS())).replaceAll("%player%", data.getPlayer().getName()).replaceAll("%client-version%", PlayerUtil.getClientVersionToString(data.getPlayer())).replaceAll("%client-brand%", data.getClientBrand()).replaceAll("%opponennt%", (data.getCombatProcessor().getTrackedPlayer() == null) ? "None" : data.getCombatProcessor().getTrackedPlayer().getName()).replaceAll("%punishment-id", Integer.toString(punishmentId)).replaceAll("%category%", StringUtils.capitalize(check.getCategory().toLowerCase())).replaceAll("%complex-type%", check.getCheckInfo().complexType()).replaceAll("%description%", check.getCheckInfo().description()).replaceAll("%check%", check.getCheckInfo().name()).replaceAll("%type%", Character.toString(check.getCheckInfo().type()))) + "#VULCAN#" + check.getDisplayName() + "#VULCAN#" + check.getDisplayType() + "#VULCAN#" + check.getVl() + "#VULCAN#" + data.getPlayer().getName() + "#VULCAN#" + check.getMaxVl());
                        } else {
                            ServerUtil.dispatchCommand(ColorUtil.translate(command.replaceAll("%player%", data.getPlayer().getName()).replaceAll("%client-version%", PlayerUtil.getClientVersionToString(data.getPlayer())).replaceAll("%client-brand%", data.getClientBrand()).replaceAll("%server-name%", Config.SERVER_NAME).replaceAll("%ping%", Integer.toString(data.getConnectionProcessor().getKeepAlivePing())).replaceAll("%tps%", MathUtil.trim(ServerUtil.getTPS())).replaceAll("%opponennt%", (data.getCombatProcessor().getTrackedPlayer() == null) ? "None" : data.getCombatProcessor().getTrackedPlayer().getName()).replaceAll("%punishment-id", Integer.toString(punishmentId)).replaceAll("%category%", StringUtils.capitalize(check.getCategory().toLowerCase())).replaceAll("%complex-type%", check.getCheckInfo().complexType()).replaceAll("%description%", check.getCheckInfo().description()).replaceAll("%check%", check.getCheckInfo().name()).replaceAll("%type%", Character.toString(check.getCheckInfo().type()))));
                        }
                    });
                }
            }.runTask(Anticheat.INSTANCE.getPlugin());
        }
        data.getChecks().forEach(check1 -> check1.setVl(0));
    }

    private void sendPluginMessage(final Player player, final String msg) {
        if (!Config.PLUGIN_MESSAGING) {
            return;
        }
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("punishment");
        out.writeUTF(msg);
        player.sendPluginMessage(Anticheat.INSTANCE.getPlugin(), "vulcan:bungee", out.toByteArray());
    }
}
