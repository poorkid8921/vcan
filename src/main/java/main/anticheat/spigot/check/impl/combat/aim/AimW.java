package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'W', complexType = "Analysis", description = "Generic rotation analysis heuristic.")
public class AimW extends AbstractCheck {
    public AimW(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final int sensitivity = this.data.getRotationProcessor().getSensitivity();
        final boolean cinematic = this.isExempt(ExemptType.CINEMATIC);
        final boolean invalid = sensitivity < -10 && deltaYaw > 1.25f && deltaPitch > 1.25f;
        if (invalid && !cinematic) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("sens=" + sensitivity + " deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
            }
        } else {
            this.decayBuffer();
        }
    }
}
