package main.anticheat.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanJudgementDayStartEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private boolean cancelled;

    public VulcanJudgementDayStartEvent() {
        super(true);
    }

    public static HandlerList getHandlerList() {
        return VulcanJudgementDayStartEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanJudgementDayStartEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
