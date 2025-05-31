package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

@CheckInfo(name = "Aim", type = 'A', complexType = "Slope", description = "Invalid pitch change.")
public class AimA extends AbstractCheck {
    public AimA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final boolean invalid = MathUtil.isExponentiallySmall(deltaPitch) && deltaYaw > 0.5f;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
            }
        } else {
            this.decayBuffer();
        }
    }
}
