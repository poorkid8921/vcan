package main.anticheat.spigot.check.impl.player.improbable;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Improbable", type = 'E', complexType = "Total", description = "Too many combined violations.")
public class ImprobableE extends AbstractCheck {
    private long lastFlag;

    public ImprobableE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final int totalViolations = this.data.getTotalViolations();
            if (System.currentTimeMillis() - this.lastFlag >= 60000L) {
                if (totalViolations > Config.IMPROBABLE_E_MAX_VIOLATIONS) {
                    this.fail("totalViolations=" + totalViolations);
                    this.lastFlag = System.currentTimeMillis();
                }
            }
        }
    }
}
