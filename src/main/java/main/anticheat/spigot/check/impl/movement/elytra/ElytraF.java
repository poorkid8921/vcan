package main.anticheat.spigot.check.impl.movement.elytra;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;

@CheckInfo(name = "Elytra", type = 'F', complexType = "Packet", description = "Sending Elytra packets too quickly.")
public class ElytraF extends AbstractCheck {
    private long lastGlide;

    public ElytraF(final PlayerData data) {
        super(data);
        this.lastGlide = 0L;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isEntityAction() && !this.teleporting()) {
            if (ServerUtil.isLowerThan1_9()) {
                return;
            }
            final WrappedPacketInEntityAction wrapper = this.data.getEntityActionWrapper();
            if (wrapper.getAction() == WrappedPacketInEntityAction.PlayerAction.START_FALL_FLYING) {
                final long delay = System.currentTimeMillis() - this.lastGlide;
                final boolean exempt = this.isExempt(ExemptType.FAST);
                if (delay < 110L && !exempt) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("delay=" + delay);
                    }
                } else {
                    this.decayBuffer();
                }
                this.lastGlide = System.currentTimeMillis();
            }
        }
    }
}
