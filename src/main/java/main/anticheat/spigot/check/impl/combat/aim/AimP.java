package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'P', complexType = "Yaw Acceleration", description = "Large yaw acceleration.")
public class AimP extends AbstractCheck {
    public AimP(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float pitch = this.data.getRotationProcessor().getPitch();
        final float yawAccel = this.data.getRotationProcessor().getYawAccel();
        final float pitchAccel = this.data.getRotationProcessor().getPitchAccel();
        final double distance = this.data.getCombatProcessor().getComplexDistance();
        final boolean invalid = yawAccel > 20.0f && pitchAccel < 0.05 && Math.abs(pitch) < 60.0f && distance > 1.0;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("yawAccel=" + yawAccel + " pitchAccel=" + pitchAccel);
            }
        } else {
            this.decayBuffer();
        }
    }
}
