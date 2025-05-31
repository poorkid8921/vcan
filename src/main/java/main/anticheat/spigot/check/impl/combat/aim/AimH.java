package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@CheckInfo(name = "Aim", type = 'H', complexType = "Negative", description = "Invalid sensitivity.")
public class AimH extends AbstractCheck {
    public AimH(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() > 3)
            return;

        final double finalSensitivity = this.data.getRotationProcessor().getFinalSensitivity();
        final double deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final Entity entity = this.data.getCombatProcessor().getTarget();
        if (entity == null || !(entity instanceof Player)) {
            return;
        }
        final Player target = (Player) entity;
        final PlayerData targetData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(target);
        if (targetData == null) {
            return;
        }
        final boolean cinematic = this.isExempt(ExemptType.CINEMATIC);
        final boolean tooLowSensitivity = this.data.getRotationProcessor().hasTooLowSensitivity();
        final boolean invalid = finalSensitivity < -1.0 && deltaPitch < 20.0 && deltaPitch > 1.25 && targetData.getPositionProcessor().getDeltaXZ() > 0.01;
        if (invalid && !cinematic && !tooLowSensitivity) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("final=" + finalSensitivity + " deltaPitch=" + deltaPitch);
            }
        } else {
            this.decayBuffer();
        }
    }
}
