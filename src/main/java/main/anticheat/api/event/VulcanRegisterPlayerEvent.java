package main.anticheat.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanRegisterPlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private boolean cancelled;

    public VulcanRegisterPlayerEvent(final Player player) {
        super(true);
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return VulcanRegisterPlayerEvent.handlers;
    }

    public HandlerList getHandlers() {
        return VulcanRegisterPlayerEvent.handlers;
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
}
