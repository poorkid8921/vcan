package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'L', complexType = "Direction", description = "Switching directions too quickly.")
public class AimL extends AbstractCheck {
    private float lastDeltaPitch;
    private int ticksSinceSwitchedDirection;

    public AimL(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float pitch = this.data.getRotationProcessor().getPitch();
        final float lastPitch = this.data.getRotationProcessor().getLastPitch();
        final float deltaPitch = pitch - lastPitch;
        if ((deltaPitch < 0.0f && this.lastDeltaPitch > 0.0f) || (deltaPitch > 0.0f && this.lastDeltaPitch < 0.0f)) {
            this.ticksSinceSwitchedDirection = 0;
        } else {
            ++this.ticksSinceSwitchedDirection;
        }
        final boolean invalid = this.ticksSinceSwitchedDirection == 0 && Math.abs(deltaPitch) > 5.0f;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaPitch=" + deltaPitch);
            }
        } else {
            this.resetBuffer();
        }
        this.lastDeltaPitch = deltaPitch;
    }
}