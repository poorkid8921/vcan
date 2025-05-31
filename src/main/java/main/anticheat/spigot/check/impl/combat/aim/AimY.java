package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'Y', complexType = "Rotation", description = "Generic rotation analysis heuristic.")
public class AimY extends AbstractCheck {
    public AimY(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final float lastFuckedYaw = this.data.getRotationProcessor().getLastFuckedPredictedYaw();
        final float fuckedYaw = this.data.getRotationProcessor().getFuckedPredictedYaw();
        final float difference = Math.abs(fuckedYaw - lastFuckedYaw);
        final double distance = this.data.getCombatProcessor().getDistance();
        final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.DEATH, ExemptType.WORLD_CHANGE) || this.data.getActionProcessor().getSinceTeleportTicks() < 20;
        if (exempt) {
            return;
        }
        if (distance > 0.6 && difference > 20.0f && distance < 10.0) {
            this.fail("diff=" + difference + " dist=" + distance);
        }
    }
}
