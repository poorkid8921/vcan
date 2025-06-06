package main.anticheat.spigot.check.impl.movement.elytra;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;

@CheckInfo(name = "Elytra", type = 'K', complexType = "Acceleration", description = "Accelerating while ascending.")
public class ElytraK extends AbstractCheck {
    public ElytraK(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !this.teleporting()) {
            if (ServerUtil.isLowerThan1_9() || !this.data.getActionProcessor().isBukkitGliding()) {
                return;
            }
            final double deltaY = this.data.getPositionProcessor().getDeltaY();
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXZ = this.data.getPositionProcessor().getLastDeltaXZ();
            final double acceleration = deltaXZ - lastDeltaXZ;
            final int glidingTicks = this.data.getPositionProcessor().getGlidingTicks();
            final boolean ascending = deltaY > 0.0;
            final boolean accelerating = acceleration > 0.005;
            final boolean exempt = this.isExempt(ExemptType.FIREWORK, ExemptType.EXPLOSION, ExemptType.RIPTIDE, ExemptType.DRAGON_DAMAGE, ExemptType.FLIGHT, ExemptType.VELOCITY, ExemptType.SLIME, ExemptType.DOLPHINS_GRACE, ExemptType.LIQUID);
            if (deltaXZ > 0.3 && ascending && accelerating && !exempt && glidingTicks > 5) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ + " accel=" + accelerating + " ticks=" + glidingTicks);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
