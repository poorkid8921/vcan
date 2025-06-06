package main.anticheat.spigot.check.impl.player.invalid;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Invalid", type = 'D', complexType = "Acceleration", description = "Large yaw changes without decelerating.")
public class InvalidD extends AbstractCheck {
    public InvalidD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPositionLook()) {
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double acceleration = this.data.getPositionProcessor().getAcceleration();
            final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
            final float lastDeltaYaw = this.data.getRotationProcessor().getLastDeltaYaw();
            final boolean invalid = acceleration < 1.0E-5 && deltaYaw > 15.0f && lastDeltaYaw > 15.0f && deltaXZ > 0.2;
            final boolean exempt = this.isExempt(ExemptType.GLIDING, ExemptType.RIPTIDE, ExemptType.ELYTRA, ExemptType.SPECTATOR, ExemptType.POWDER_SNOW, ExemptType.WEB);
            if (invalid && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaYaw=" + deltaYaw + " acceleration=" + acceleration);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
