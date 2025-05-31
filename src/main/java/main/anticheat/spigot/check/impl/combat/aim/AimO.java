package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'O', complexType = "Small Yaw", description = "Too small pitch change.")
public class AimO extends AbstractCheck {
    public AimO(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final boolean invalid = deltaPitch < 0.05 && deltaPitch > 0.0f && deltaYaw == 0.0f;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaPitch=" + deltaPitch + " deltaYaw=" + deltaYaw);
            }
        } else {
            this.decayBuffer();
        }
    }
}
