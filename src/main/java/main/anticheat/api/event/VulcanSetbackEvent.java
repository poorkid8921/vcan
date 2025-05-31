package main.anticheat.api.event;

import main.anticheat.api.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanSetbackEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private final Check check;
    private final long timestamp;
    private boolean cancelled;

    public VulcanSetbackEvent(final Player player, final Check check) {
        super(true);
        this.timestamp = System.currentTimeMillis();
        this.player = player;
        this.check = check;
    }

    public static HandlerList getHandlerList() {
        return VulcanSetbackEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanSetbackEvent.handlers;
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

    public long getTimestamp() {
        return this.timestamp;
    }
}
