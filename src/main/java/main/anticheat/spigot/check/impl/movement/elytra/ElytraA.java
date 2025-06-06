package main.anticheat.spigot.check.impl.movement.elytra;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;

@CheckInfo(name = "Elytra", type = 'A', complexType = "Limit", description = "Gliding too quickly on X/Z axis.")
public class ElytraA extends AbstractCheck {
    public ElytraA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !this.teleporting()) {
            if (ServerUtil.isLowerThan1_9() || !this.data.getActionProcessor().isBukkitGliding()) {
                return;
            }
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final boolean exempt = this.isExempt(ExemptType.RIPTIDE, ExemptType.TELEPORT, ExemptType.WORLD_CHANGE, ExemptType.DEATH, ExemptType.ENDER_PEARL, ExemptType.CHORUS_FRUIT, ExemptType.SERVER_POSITION_FAST, ExemptType.DRAGON_DAMAGE, ExemptType.BUKKIT_VELOCITY, ExemptType.DOLPHINS_GRACE) || this.data.getVelocityProcessor().getSinceHighVelocityTicks() < 50 || this.data.getPositionProcessor().getSinceFuckingEntityTicks() < 200;
            final boolean riptide = this.data.getPositionProcessor().getSinceRiptideTicks() < 400;
            if (deltaXZ > Config.ELYTRA_A_MAX_SPEED && !exempt && !riptide) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
