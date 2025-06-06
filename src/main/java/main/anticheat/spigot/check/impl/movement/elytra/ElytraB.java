package main.anticheat.spigot.check.impl.movement.elytra;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;

@CheckInfo(name = "Elytra", type = 'B', complexType = "Acceleration", description = "Invalid Y-Axis change.")
public class ElytraB extends AbstractCheck {
    public ElytraB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            if (ServerUtil.isLowerThan1_9() || !this.data.getActionProcessor().isBukkitGliding()) {
                return;
            }
            final int glidingTicks = this.data.getPositionProcessor().getGlidingTicks();
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXZ = this.data.getPositionProcessor().getLastDeltaXZ();
            final double accelerationXZ = Math.abs(deltaXZ - lastDeltaXZ);
            final double deltaY = this.data.getPositionProcessor().getDeltaY();
            final double lastDeltaY = this.data.getPositionProcessor().getLastDeltaY();
            final double accelerationY = Math.abs(deltaY - lastDeltaY);
            final boolean exempt = this.isExempt(ExemptType.FIREWORK, ExemptType.LIQUID);
            final boolean invalid = glidingTicks > 5 && glidingTicks < 400 && accelerationXZ < 1.0E-10 && accelerationY < 1.0E-10 && deltaXZ > 1.0;
            if (invalid && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("ticks=" + glidingTicks + " deltaXZ=" + deltaXZ + " deltaY=" + deltaY + " accelXZ=" + accelerationXZ + " accelY=" + accelerationY);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
