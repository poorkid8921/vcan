package main.anticheat.api.event;

import main.anticheat.api.check.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanFlagEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private final Check check;
    private final String info;
    private final long timestamp;
    private boolean cancelled;

    public VulcanFlagEvent(final Player player, final Check check, final String info) {
        super(true);
        this.timestamp = System.currentTimeMillis();
        this.player = player;
        this.check = check;
        this.info = info;
    }

    public static HandlerList getHandlerList() {
        return VulcanFlagEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanFlagEvent.handlers;
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

    public String getInfo() {
        return this.info;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
