package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

@CheckInfo(name = "Aim", type = 'I', complexType = "Constant", description = "Not constant rotations.")
public class AimI extends AbstractCheck {
    public AimI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final float lastDeltaPitch = this.data.getRotationProcessor().getLastDeltaPitch();
        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final long expandedPitch = (long) (MathUtil.EXPANDER * deltaPitch);
        final long expandedLastPitch = (long) (MathUtil.EXPANDER * lastDeltaPitch);
        final boolean cinematic = this.isExempt(ExemptType.CINEMATIC);
        final long gcd = MathUtil.getGcd(expandedPitch, expandedLastPitch);
        final boolean tooLowSensitivity = this.data.getRotationProcessor().hasTooLowSensitivity();
        final boolean validAngles = deltaYaw > 0.25f && deltaPitch > 0.25f && deltaPitch < 20.0f && deltaYaw < 20.0f;
        final boolean invalid = !cinematic && gcd < 131072L;
        if (invalid && validAngles && !tooLowSensitivity) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("rotation=" + gcd / 1000L + " deltaPitch=" + deltaPitch + " deltaYaw=" + deltaYaw);
            }
        } else {
            this.decayBuffer();
        }
    }
}