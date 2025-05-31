package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'B', complexType = "Modulo", description = "Invalid yaw change.")
public class AimB extends AbstractCheck {
    public AimB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final boolean invalid = deltaYaw > 0.0f && (deltaYaw % 0.25 == 0.0 || deltaYaw % 0.1 == 0.0);
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaYaw=" + deltaYaw);
            }
        } else {
            this.decayBuffer();
        }
    }
}
