package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'I', complexType = "Entity Action", description = "Sent EntityAction while attacking.")
public class BadPacketsI extends AbstractCheck {
    public BadPacketsI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final boolean invalid = this.data.getActionProcessor().isSendingAction();
            final boolean exempt = this.isExempt(ExemptType.SERVER_VERSION, ExemptType.CLIENT_VERSION, ExemptType.FAST, ExemptType.SPECTATOR, ExemptType.DEATH);
            if (invalid) {
                if (!exempt) {
                    this.fail();
                }
            }
        }
    }
}
