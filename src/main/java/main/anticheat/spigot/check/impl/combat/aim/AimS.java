package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'S', complexType = "Divisor Y", description = "Subtle aim assist modifications.")
public class AimS extends AbstractCheck {
    public AimS(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final boolean validSensitivity = this.data.getRotationProcessor().hasValidSensitivity();
        if (validSensitivity) {
            final double mcpSensitivity = this.data.getRotationProcessor().getMcpSensitivity();
            if (mcpSensitivity < 0.01) {
                return;
            }
            final float f = (float) mcpSensitivity * 0.6f + 0.2f;
            final float gcd = f * f * f * 1.2f;
            if (gcd < 1.0E-4) {
                return;
            }
            final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
            final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
            final double deltaX = deltaYaw / gcd;
            final double deltaY = deltaPitch / gcd;
            final double floorDivisorX = Math.abs(Math.round(deltaX) - deltaX);
            final double floorDivisorY = Math.abs(Math.round(deltaY) - deltaY);
            if (floorDivisorY > 0.03 && floorDivisorX < 1.0E-4) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("divisorX=" + floorDivisorX + " divisorY=" + floorDivisorY);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
