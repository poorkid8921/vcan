package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'J', complexType = "Held Item Slot", description = "Sent HeldItemSlot while placing.")
public class BadPacketsJ extends AbstractCheck {
    public BadPacketsJ(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isHeldItemSlot()) {
            final boolean placing = this.data.getActionProcessor().isPlacing();
            final boolean exempt = this.isExempt(ExemptType.CLIENT_VERSION, ExemptType.SERVER_VERSION, ExemptType.CREATIVE, ExemptType.SPECTATOR);
            if (!exempt) {
                if (placing) {
                    this.fail();
                }
            }
        }
    }
}
