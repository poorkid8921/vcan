package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'E', complexType = "Interact", description = "Interacted with themselves.")
public class BadPacketsE extends AbstractCheck {
    public BadPacketsE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            final boolean exempt = this.isExempt(ExemptType.SPECTATOR);
            if (wrapper.getEntityId() == this.data.getPlayer().getEntityId() && !exempt) {
                this.fail();
            }
        }
    }
}
