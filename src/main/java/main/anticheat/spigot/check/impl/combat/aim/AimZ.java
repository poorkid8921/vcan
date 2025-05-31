package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'Z', complexType = "Modulo 360", description = "360 bypass flaw detected.")
public class AimZ extends AbstractCheck {
    public AimZ(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation())
            return;

        float lastDeltaYaw = this.data.getRotationProcessor().getLastDeltaYaw();
        if (this.data.getActionProcessor().isTeleporting() || this.hitTicks() > 3) return;

        float yaw = this.data.getRotationProcessor().getYaw();
        float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();

        if (yaw < 360 && yaw > -360 && Math.abs(deltaYaw) > 320 && Math.abs(lastDeltaYaw) < 30) {
            float yawDifference = Math.abs(yaw - this.data.getRotationProcessor().getLastYaw());

            float lastPitch = this.data.getRotationProcessor().getLastPitch();
            float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();

            float pitchDifference = Math.abs(lastPitch - deltaPitch);
            float combinedChange = Math.abs(deltaYaw + deltaPitch);

            this.fail("yawDifference=" + yawDifference + " pitchDifference=" + pitchDifference + " combined=" + combinedChange);
        } else {
            this.decayBuffer();
        }
    }
}
