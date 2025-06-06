package main.anticheat.spigot.check.impl.player.invalid;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Invalid", type = 'E', complexType = "X/Z", description = "Too large X/Z-Axis movement.")
public class InvalidE extends AbstractCheck {
    public InvalidE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !this.teleporting() && !this.fuckedPosition()) {
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.RIPTIDE, ExemptType.GLIDING, ExemptType.ATTRIBUTE_MODIFIER, ExemptType.VELOCITY, ExemptType.JOINED, ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.VEHICLE, ExemptType.WORLD_CHANGE, ExemptType.DOLPHINS_GRACE, ExemptType.SOUL_SPEED, ExemptType.CHORUS_FRUIT, ExemptType.RIPTIDE, ExemptType.SLEEPING, ExemptType.LEVITATION, ExemptType.ENDER_PEARL, ExemptType.ENTITY_CRAM_FIX, ExemptType.HIGH_SPEED, ExemptType.SHULKER_BOX, ExemptType.SHULKER, ExemptType.CHUNK);
            final boolean invalid = deltaXZ > 3.5 && deltaXZ < 100.0;
            if (invalid && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
