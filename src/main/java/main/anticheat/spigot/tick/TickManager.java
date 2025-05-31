package main.anticheat.spigot.tick;

import lombok.Getter;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

@Getter
public class TickManager implements Runnable {
    private static BukkitTask task;
    private int ticks;

    public void start() {
        assert TickManager.task == null : "TickProcessor has already been started!";
        TickManager.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Anticheat.INSTANCE.getPlugin(), this, 0L, 1L);
    }

    public void stop() {
        if (TickManager.task == null) {
            return;
        }
        TickManager.task.cancel();
        TickManager.task = null;
    }

    @Override
    public void run() {
        ++this.ticks;
        if (this.ticks % 5 == 0) {
            final Iterator<PlayerData> iterator = Anticheat.INSTANCE.getPlayerDataManager().getAllData().iterator();
            PlayerData data;
            while (iterator.hasNext()) {
                data = iterator.next();
                data.getConnectionProcessor().sendPing();
            }
            Anticheat.INSTANCE.getPlayerDataManager().getAllData().forEach(data2 -> data2.getConnectionProcessor().sendPing());
        }
        if (Config.PUNISHMENT_STATISTIC_BROADCAST && this.ticks % Config.PUNISHMENT_STATISTIC_BROADCAST_INTERVAL == 0) {
            Config.PUNISHMENT_STATISTIC_BROADCAST_MESSAGE.forEach(ServerUtil::broadcast);
        }
        if (this.ticks > 4000 && this.ticks % 500 == 0 && ServerUtil.getTPS() < 1.0 && Config.INCOMPATABILITY_MANAGER) {
            Anticheat.INSTANCE.log("Incompatible Spigot fork detected. Please contact support @ https://discord.com/invite/SCNuwUG");
        }
    }
}
