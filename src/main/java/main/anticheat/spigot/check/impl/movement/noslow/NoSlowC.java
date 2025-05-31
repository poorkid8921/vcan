package main.anticheat.spigot.check.impl.movement.noslow;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;

@CheckInfo(name = "No Slow", type = 'C', complexType = "Web", description = "Moving too quickly in a web.")
public class NoSlowC extends AbstractCheck {
    public NoSlowC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double max = PlayerUtil.getBaseSpeed(this.data, 0.09f);
            final boolean inWeb = this.data.getPositionProcessor().isInWeb();
            final boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.ENDER_PEARL, ExemptType.CHORUS_FRUIT, ExemptType.RIPTIDE, ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.DEPTH_STRIDER, ExemptType.SERVER_POSITION_FAST, ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.ATTRIBUTE_MODIFIER);
            final boolean invalid = inWeb && deltaXZ > max;
            if (invalid && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ + " max=" + max);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
