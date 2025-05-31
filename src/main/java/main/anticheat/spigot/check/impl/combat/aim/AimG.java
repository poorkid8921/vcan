package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'G', complexType = "Ratio", description = "Too large yaw change.")
public class AimG extends AbstractCheck {
    public AimG(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || !this.attacking(2))
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final float pitch = this.data.getRotationProcessor().getPitch();
        final boolean invalid = deltaYaw > 15.0f && deltaPitch < 0.1 && Math.abs(pitch) < 65.0f;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
            }
        } else {
            this.decayBuffer();
        }
    }
}
