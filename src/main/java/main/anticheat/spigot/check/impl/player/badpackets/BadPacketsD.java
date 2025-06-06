package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.GameRule;

@CheckInfo(name = "Bad Packets", type = 'D', complexType = "Respawn", description = "Spoofed Respawn packet.")
public class BadPacketsD extends AbstractCheck {
    public BadPacketsD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isClientCommand()) {
            final WrappedPacketInClientCommand wrapper = new WrappedPacketInClientCommand(packet.getRawPacket());
            final boolean exempt = this.isExempt(ExemptType.DEATH, ExemptType.WORLD_CHANGE, ExemptType.TELEPORT, ExemptType.JOINED);
            if (wrapper.getClientCommand() == WrappedPacketInClientCommand.ClientCommand.PERFORM_RESPAWN) {
                if (!exempt) {
                    final double health = this.data.getPlayer().getHealth();
                    if (ServerUtil.isHigherThan1_16() && (boolean) this.data.getPlayer().getWorld().getGameRuleValue(GameRule.DO_IMMEDIATE_RESPAWN)) {
                        return;
                    }
                    if (health > 4.0) {
                        if (this.increaseBuffer() > this.MAX_BUFFER) {
                            this.fail("health=" + health);
                        }
                    } else {
                        this.decayBuffer();
                    }
                }
            }
        } else if (packet.isFlying()) {
            this.decayBuffer();
        }
    }
}
