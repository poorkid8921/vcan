package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'N', complexType = "Small Yaw", description = "Too small yaw change.")
public class AimN extends AbstractCheck {
    public AimN(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final boolean invalid = deltaYaw < 0.05 && deltaYaw > 0.0f && deltaPitch == 0.0f;
        final boolean exempt = this.isExempt(ExemptType.VEHICLE, ExemptType.BOAT);
        if (invalid && !exempt) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
            }
        } else {
            this.decayBuffer();
        }
    }
}
