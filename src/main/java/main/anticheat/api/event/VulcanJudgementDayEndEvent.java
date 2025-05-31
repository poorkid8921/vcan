package main.anticheat.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanJudgementDayEndEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private boolean cancelled;

    public VulcanJudgementDayEndEvent() {
        super(true);
    }

    public static HandlerList getHandlerList() {
        return VulcanJudgementDayEndEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanJudgementDayEndEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
