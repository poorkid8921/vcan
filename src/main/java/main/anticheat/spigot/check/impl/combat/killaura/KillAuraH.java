package main.anticheat.spigot.check.impl.combat.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;

@CheckInfo(name = "Kill Aura", type = 'H', complexType = "Inventory", description = "Attacked while opening inventory.")
public class KillAuraH extends AbstractCheck {
    private boolean attacked;

    public KillAuraH(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            this.attacked = false;
        } else if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                this.attacked = true;
            }
        } else if (packet.isClientCommand()) {
            final WrappedPacketInClientCommand wrapper2 = new WrappedPacketInClientCommand(packet.getRawPacket());
            if (wrapper2.getClientCommand() == WrappedPacketInClientCommand.ClientCommand.OPEN_INVENTORY_ACHIEVEMENT && this.attacked) {
                if (!PlayerUtil.isHigherThan1_9(this.data.getPlayer())) {
                    this.fail();
                }
            }
        }
    }
}
