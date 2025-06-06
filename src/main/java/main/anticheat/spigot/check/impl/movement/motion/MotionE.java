package main.anticheat.spigot.check.impl.movement.motion;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Motion", type = 'E', complexType = "Modulo", description = "Impossible movement values.")
public class MotionE extends AbstractCheck {
    public MotionE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            if (this.data.getActionProcessor().isHasSlowness() || this.data.getPlayer().getWalkSpeed() > 0.25) {
                return;
            }
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double modulo = 0.1 % (deltaXZ % 0.1);
            final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.RIPTIDE, ExemptType.GLIDING, ExemptType.STAIRS, ExemptType.VELOCITY, ExemptType.JOINED, ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.VEHICLE, ExemptType.WORLD_CHANGE, ExemptType.DOLPHINS_GRACE, ExemptType.SOUL_SPEED, ExemptType.SLIME, ExemptType.HONEY, ExemptType.DEATH, ExemptType.RIPTIDE, ExemptType.SLEEPING, ExemptType.LEVITATION, ExemptType.LIQUID, ExemptType.ANVIL, ExemptType.SERVER_POSITION, ExemptType.PISTON, ExemptType.WEB);
            final boolean invalid = deltaXZ > 0.11 && modulo < 1.0E-8;
            if (invalid && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ + " modulo=" + modulo);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
