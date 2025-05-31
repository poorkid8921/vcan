package main.anticheat;

import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import main.anticheat.api.PluginAPI;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.api.SpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

@Getter
public final class MainPlugin extends JavaPlugin {
    public static JavaPlugin INSTANCE;
    public static BukkitScheduler SCHEDULER;

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEvents.get().getSettings().checkForUpdates(false);
        PacketEvents.get().load();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        SCHEDULER = Bukkit.getScheduler();

        PacketEvents.get().init();
        Anticheat.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
        Anticheat.INSTANCE.stop(this);
    }
}
