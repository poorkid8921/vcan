package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'O', description = "Negative HeldItemSlot packet.")
public class BadPacketsO extends AbstractCheck {
    public BadPacketsO(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isHeldItemSlot()) {
            final WrappedPacketInHeldItemSlot wrapper = this.data.getHeldItemSlotWrapper();
            final int slot = wrapper.getCurrentSelectedSlot();
            if (slot < 0) {
                this.fail("slot=" + slot);
            }
        }
    }
}
