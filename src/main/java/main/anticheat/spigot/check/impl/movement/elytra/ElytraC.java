package main.anticheat.spigot.check.impl.movement.elytra;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;
import main.anticheat.spigot.util.ServerUtil;

@CheckInfo(name = "Elytra", type = 'C', complexType = "Acceleration", description = "Invalid acceleration.")
public class ElytraC extends AbstractCheck {
    public ElytraC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            if (ServerUtil.isLowerThan1_9() || !this.data.getActionProcessor().isBukkitGliding()) {
                return;
            }
            final int glidingTicks = this.data.getPositionProcessor().getGlidingTicks();
            final int airTicks = this.data.getPositionProcessor().getServerAirTicks();
            final double deltaY = this.data.getPositionProcessor().getDeltaY();
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXZ = this.data.getPositionProcessor().getLastDeltaXZ();
            final boolean gliding = PlayerUtil.isGliding(this.data.getPlayer());
            final boolean exempt = this.isExempt(ExemptType.FIREWORK, ExemptType.EXPLOSION, ExemptType.RIPTIDE, ExemptType.DRAGON_DAMAGE, ExemptType.FLIGHT, ExemptType.SLIME) || this.data.getVelocityProcessor().getSinceHighVelocityTicks() < 50;
            final double acceleration = deltaXZ - lastDeltaXZ;
            if (glidingTicks > 3 && airTicks > 3 && gliding && !exempt && acceleration > 0.035 && deltaY > -0.6) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("accel=" + acceleration + " deltaXZ=" + deltaXZ + " deltaY=" + deltaY);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
