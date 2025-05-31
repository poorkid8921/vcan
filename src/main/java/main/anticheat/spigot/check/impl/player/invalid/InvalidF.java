package main.anticheat.spigot.check.impl.player.invalid;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Invalid", type = 'F', complexType = "Spoofed Y", description = "Impossible Y-axis change.")
public class InvalidF extends AbstractCheck {
    public InvalidF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !this.teleporting()) {
            final double deltaY = this.data.getPositionProcessor().getDeltaY();
            final boolean exempt = this.isExempt(ExemptType.DEATH, ExemptType.JOINED, ExemptType.TELEPORT, ExemptType.WORLD_CHANGE);
            if (Math.abs(deltaY) > 100.0 && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaY=" + deltaY);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
