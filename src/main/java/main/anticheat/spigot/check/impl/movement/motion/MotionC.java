package main.anticheat.spigot.check.impl.movement.motion;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Motion", type = 'C', complexType = "Jump", description = "Invalid jump motion.")
public class MotionC extends AbstractCheck {
    public MotionC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaY = Math.abs(this.data.getPositionProcessor().getDeltaY());
            final double difference = Math.abs(deltaY - 0.41999998688697815);
            final boolean exempt = this.isExempt(ExemptType.JUMP_BOOST, ExemptType.VELOCITY, ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.SLIME, ExemptType.GLIDING, ExemptType.WEB, ExemptType.VEHICLE, ExemptType.SCAFFOLDING, ExemptType.LIQUID, ExemptType.SLAB, ExemptType.STAIRS, ExemptType.ELYTRA);
            final boolean invalid = difference < 1.0E-5 && difference > 0.0 && deltaY > 0.1;
            if (invalid && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaY=" + deltaY);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
