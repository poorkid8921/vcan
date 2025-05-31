package main.anticheat.spigot.check.impl.player.improbable;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Improbable", type = 'F', complexType = "Scaffold", description = "Too many combined Scaffold violations.")
public class ImprobableF extends AbstractCheck {
    private long lastFlag;

    public ImprobableF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final int scaffoldViolations = this.data.getScaffoldViolations();
            if (System.currentTimeMillis() - this.lastFlag >= 60000L) {
                if (scaffoldViolations > Config.IMPROBABLE_F_MAX_SCAFFOLD_VIOLATIONS) {
                    this.fail("scaffoldViolations=" + scaffoldViolations);
                    this.lastFlag = System.currentTimeMillis();
                }
            }
        }
    }
}
