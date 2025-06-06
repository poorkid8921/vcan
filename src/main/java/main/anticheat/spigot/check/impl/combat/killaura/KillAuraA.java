package main.anticheat.spigot.check.impl.combat.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Kill Aura", type = 'A', complexType = "Post", description = "Post UseEntity packets.")
public class KillAuraA extends AbstractCheck {
    private long lastFlying;
    private boolean sent;

    public KillAuraA(final PlayerData data) {
        super(data);
        this.lastFlying = 0L;
        this.sent = false;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final long delay = System.currentTimeMillis() - this.lastFlying;
            if (this.sent) {
                final boolean exempt = this.isExempt(ExemptType.SPECTATOR);
                if (delay > 40L && delay < 100L && !exempt) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("delay=" + delay);
                    }
                } else {
                    this.decayBuffer();
                }
                this.sent = false;
            }
            this.lastFlying = System.currentTimeMillis();
        } else if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                return;
            }
            final long delay2 = System.currentTimeMillis() - this.lastFlying;
            if (delay2 < 10L) {
                this.sent = true;
            }
        }
    }
}
