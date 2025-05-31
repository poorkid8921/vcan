package main.anticheat.spigot.hook.gsit;

import dev.geco.gsit.api.event.PlayerCrawlEvent;
import dev.geco.gsit.api.event.PlayerGetUpCrawlEvent;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GSitListener implements Listener {
    @EventHandler
    public void onCrawl(final PlayerCrawlEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().setCrawling(true);
    }

    @EventHandler
    public void onCrawl(final PlayerGetUpCrawlEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().setCrawling(false);
    }
}
