package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'C', complexType = "Pitch", description = "Impossible pitch.")
public class BadPacketsC extends AbstractCheck {
    public BadPacketsC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float pitch = this.data.getRotationProcessor().getPitch();
            if (Math.abs(pitch) > 90.0f) {
                this.fail("pitch=" + pitch);
            }
        }
    }
}
