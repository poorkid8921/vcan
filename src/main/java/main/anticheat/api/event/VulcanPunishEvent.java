package main.anticheat.api.event;

import main.anticheat.api.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanPunishEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private final Check check;
    private boolean cancelled;

    public VulcanPunishEvent(final Player player, final Check check) {
        super(true);
        this.player = player;
        this.check = check;
    }

    public static HandlerList getHandlerList() {
        return VulcanPunishEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanPunishEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Check getCheck() {
        return this.check;
    }
}
