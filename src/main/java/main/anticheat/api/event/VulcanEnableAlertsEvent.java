package main.anticheat.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanEnableAlertsEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private final long timestamp;
    private boolean cancelled;

    public VulcanEnableAlertsEvent(final Player player) {
        this.timestamp = System.currentTimeMillis();
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return VulcanEnableAlertsEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanEnableAlertsEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
