package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'U', complexType = "GCD Flaw", description = "GCD bypass.")
public class AimU extends AbstractCheck {
    public AimU(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final boolean validSensitivity = this.data.getRotationProcessor().hasValidSensitivity();
        if (validSensitivity) {
            final double mcpSensitivity = this.data.getRotationProcessor().getMcpSensitivity();
            final float f = (float) mcpSensitivity * 0.6f + 0.2f;
            final float gcd = f * f * f * 1.2f;
            final float yaw = this.data.getRotationProcessor().getYaw();
            final float pitch = this.data.getRotationProcessor().getPitch();
            final float adjustedYaw = yaw - yaw % gcd;
            final float adjustedPitch = pitch - pitch % gcd;
            final float yawDifference = Math.abs(yaw - adjustedYaw);
            final float pitchDifference = Math.abs(pitch - adjustedPitch);
            if (yawDifference == 0.01f || pitchDifference == 0.01f) {
                this.fail("[I] yawDiff=" + yawDifference + " pitchDiff=" + pitchDifference);
            }
        }
    }
}