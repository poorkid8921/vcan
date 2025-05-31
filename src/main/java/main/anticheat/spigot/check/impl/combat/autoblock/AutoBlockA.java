package main.anticheat.spigot.check.impl.combat.autoblock;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Auto Block", type = 'A', complexType = "Sequence", description = "Attacked while sending BlockPlace packet.")
public class AutoBlockA extends AbstractCheck {
    public AutoBlockA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                final boolean invalid = this.data.getActionProcessor().isPlacing();
                final boolean exempt = this.isExempt(ExemptType.CLIENT_VERSION);
                if (invalid && !exempt) {
                    this.fail();
                }
            }
        }
    }
}
