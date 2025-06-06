package main.anticheat.spigot.check.impl.player.groundspoof;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Ground Spoof", type = 'B', complexType = "Spoof", description = "Spoofed OnGround value.")
public class GroundSpoofB extends AbstractCheck {
    public GroundSpoofB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final boolean serverOnGround = this.data.getPositionProcessor().isMathematicallyOnGround();
            final boolean clientOnGround = this.data.getPositionProcessor().isClientOnGround();
            final boolean touchingAir = this.data.getPositionProcessor().isTouchingAir();
            final boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.BOAT, ExemptType.TELEPORT, ExemptType.SHULKER, ExemptType.SHULKER_BOX, ExemptType.CHUNK, ExemptType.CARPET, ExemptType.VEHICLE, ExemptType.SCAFFOLDING, ExemptType.DEAD);
            final double y = this.data.getPositionProcessor().getY();
            final boolean boat = y % 1.0 == 0.6 || y % 0.1 == 0.5625;
            if (clientOnGround && !serverOnGround && !exempt && touchingAir && !boat) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("client=" + clientOnGround + " server=" + serverOnGround);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
