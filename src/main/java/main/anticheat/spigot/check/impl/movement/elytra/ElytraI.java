package main.anticheat.spigot.check.impl.movement.elytra;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;

@CheckInfo(name = "Elytra", type = 'I', complexType = "Motion", description = "Invalid motion.")
public class ElytraI extends AbstractCheck {
    public ElytraI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !this.teleporting()) {
            if (ServerUtil.isLowerThan1_9() || !this.data.getActionProcessor().isBukkitGliding()) {
                return;
            }
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double modulo = 0.1 % (deltaXZ % 0.1);
            final boolean exempt = this.isExempt(ExemptType.FIREWORK, ExemptType.EXPLOSION, ExemptType.RIPTIDE, ExemptType.DRAGON_DAMAGE, ExemptType.FLIGHT, ExemptType.VELOCITY, ExemptType.LIQUID);
            if (modulo < 1.0E-8 && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
