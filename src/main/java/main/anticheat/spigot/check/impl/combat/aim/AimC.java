package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'C', complexType = "Repeated", description = "Repeated yaw values.")
public class AimC extends AbstractCheck {
    public AimC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || !this.attacking(3))
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float lastDeltaYaw = this.data.getRotationProcessor().getLastDeltaYaw();
        final boolean invalid = deltaYaw > 1.25f && lastDeltaYaw > 1.25f && deltaYaw == lastDeltaYaw;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaYaw=" + deltaYaw);
            }
        } else {
            this.decayBuffer();
        }
    }
}
