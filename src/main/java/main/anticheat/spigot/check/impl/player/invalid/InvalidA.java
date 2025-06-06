package main.anticheat.spigot.check.impl.player.invalid;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;

@CheckInfo(name = "Invalid", type = 'A', complexType = "A", description = "Moving too quickly.", experimental = true)
public class InvalidA extends AbstractCheck {
    public InvalidA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !this.teleporting()) {
            final boolean flight = this.isExempt(ExemptType.FLIGHT) && !this.data.getPlayer().getAllowFlight();
            final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.WORLD_CHANGE, ExemptType.JOINED, ExemptType.VEHICLE, ExemptType.RESPAWN, ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.FIREWORK, ExemptType.RIPTIDE);
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double flySpeed = this.data.getPositionProcessor().getFlySpeed();
            double max = PlayerUtil.getBaseSpeed(this.data, 1.2f);
            if (this.data.getVelocityProcessor().getTransactionFlyingTicks() < 30) {
                max += this.data.getVelocityProcessor().getVelocityXZ() + 0.05;
            }
            final double difference = deltaXZ - max;
            final int sinceTeleportTicks = this.data.getActionProcessor().getSinceTeleportTicks();
            final boolean spectator = this.data.getPositionProcessor().getSinceSpectatorTicks() < 50;
            if (!exempt && flight && deltaXZ > max && flySpeed < 0.10999999940395355 && !spectator) {
                if (this.increaseBuffer() > this.MAX_BUFFER || (difference > 1.0 && sinceTeleportTicks > 5)) {
                    this.fail("dXZ=" + deltaXZ + " max=" + max);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
