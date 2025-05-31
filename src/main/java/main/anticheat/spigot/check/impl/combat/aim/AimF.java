package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@CheckInfo(name = "Aim", type = 'F', complexType = "Straight", description = "Invalid yaw change.")
public class AimF extends AbstractCheck {
    public AimF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || !this.attacking(3))
            return;

        final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
        final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
        final float pitch = this.data.getRotationProcessor().getPitch();
        final double distance = this.data.getCombatProcessor().getDistance();
        final Entity entity = this.data.getCombatProcessor().getTarget();
        if (entity == null || !(entity instanceof Player)) {
            return;
        }
        final Player target = (Player) entity;
        final PlayerData targetData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(target);
        if (targetData == null) {
            return;
        }
        final boolean invalid = deltaPitch == 0.0f && deltaYaw > 1.825f && Math.abs(pitch) < 60.0f && distance > 1.0 && targetData.getPositionProcessor().getDeltaXZ() > 0.03;
        if (invalid) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.fail("deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
            }
        } else {
            this.decayBuffer();
        }
    }
}
