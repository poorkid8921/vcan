package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'H', complexType = "Packet Order", description = "Invalid attack packet order.")
public class BadPacketsH extends AbstractCheck {
    private boolean swung;

    public BadPacketsH(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            final boolean exempt = this.isExempt(ExemptType.SERVER_VERSION, ExemptType.CLIENT_VERSION, ExemptType.FAST);
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                if (!exempt) {
                    if (!this.swung) {
                        this.fail("swung=" + this.swung);
                    }
                }
            }
        } else if (packet.isArmAnimation()) {
            this.swung = true;
        } else if (packet.isFlying()) {
            this.swung = false;
        }
    }
}
