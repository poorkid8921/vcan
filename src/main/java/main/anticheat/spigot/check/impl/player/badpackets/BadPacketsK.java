package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'K', complexType = "Sneak", description = "Sending EntityAction packets too quickly")
public class BadPacketsK extends AbstractCheck {
    private long lastSneak;

    public BadPacketsK(final PlayerData data) {
        super(data);
        this.lastSneak = -1L;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isEntityAction()) {
            final WrappedPacketInEntityAction wrapper = this.data.getEntityActionWrapper();
            if (wrapper.getAction() == WrappedPacketInEntityAction.PlayerAction.START_SNEAKING) {
                final long now = System.currentTimeMillis();
                final long delay = now - this.lastSneak;
                final boolean exempt = this.isExempt(ExemptType.FAST);
                if (delay < 90L && !exempt) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("delay=" + delay);
                    }
                } else {
                    this.decayBuffer();
                }
                this.lastSneak = now;
            }
        }
    }
}
