package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.GameMode;

@CheckInfo(name = "Bad Packets", type = 'Z', complexType = "Spectate", description = "Invalid Spectate Packets.")
public class BadPacketsZ extends AbstractCheck {
    public BadPacketsZ(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isSpectate()) {
            if (ServerUtil.isLowerThan1_8()) {
                return;
            }
            final boolean invalid = this.data.getPlayer().getGameMode() != GameMode.SPECTATOR && this.data.getActionProcessor().getGameMode() != GameMode.SPECTATOR;
            if (invalid) {
                this.fail();
            }
        }
    }
}
