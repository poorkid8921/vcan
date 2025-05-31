package main.anticheat.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanGhostBlockEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private final long timestamp;
    private boolean cancelled;

    public VulcanGhostBlockEvent(final Player player) {
        super(true);
        this.timestamp = System.currentTimeMillis();
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return VulcanGhostBlockEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanGhostBlockEvent.handlers;
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

    public long getTimestamp() {
        return this.timestamp;
    }
}
