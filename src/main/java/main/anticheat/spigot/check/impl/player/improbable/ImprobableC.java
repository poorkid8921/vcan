package main.anticheat.spigot.check.impl.player.improbable;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Improbable", type = 'C', complexType = "Player", description = "Too many combined player violations.")
public class ImprobableC extends AbstractCheck {
    private long lastFlag;

    public ImprobableC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final int playerViolations = this.data.getPlayerViolations();
            if (System.currentTimeMillis() - this.lastFlag >= 60000L) {
                if (playerViolations > Config.IMPROBABLE_C_MAX_VIOLATIONS) {
                    this.fail("playerViolations=" + playerViolations);
                    this.lastFlag = System.currentTimeMillis();
                }
            }
        }
    }
}
