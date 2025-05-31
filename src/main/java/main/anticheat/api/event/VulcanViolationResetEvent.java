package main.anticheat.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanViolationResetEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return VulcanViolationResetEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanViolationResetEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
