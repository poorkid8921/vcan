package main.anticheat.spigot.listener;

import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.ColorUtil;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (data.getPositionProcessor().isFrozen()) {
            Anticheat.INSTANCE.getAlertManager().sendMessage(Config.LOGGED_OUT_WHILE_FROZEN.replaceAll("%player%", player.getName()));
            Config.LOGGED_OUT_FROZEN_COMMANDS.forEach(command -> ServerUtil.dispatchCommand(ColorUtil.translate(command.replaceAll("%player%", player.getName()))));
        }
        Anticheat.INSTANCE.getAlertManager().getAlertsEnabled().remove(player);
        Anticheat.INSTANCE.getAlertManager().getVerboseEnabled().remove(player);
        Anticheat.INSTANCE.getPlayerDataManager().remove(player);
    }
}
