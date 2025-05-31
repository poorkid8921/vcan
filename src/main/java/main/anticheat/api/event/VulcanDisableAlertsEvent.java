package main.anticheat.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class VulcanDisableAlertsEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final Player player;
    private final long timestamp;
    @Setter
    private boolean cancelled;

    public VulcanDisableAlertsEvent(final Player player) {
        this.timestamp = System.currentTimeMillis();
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return VulcanDisableAlertsEvent.handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return VulcanDisableAlertsEvent.handlers;
    }
}
