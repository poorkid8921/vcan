package main.anticheat.spigot.check.impl.player.improbable;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Improbable", type = 'B', complexType = "Movement", description = "Too many combined movement violations.")
public class ImprobableB extends AbstractCheck {
    private long lastFlag;

    public ImprobableB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final int movementViolations = this.data.getMovementViolations();
            if (System.currentTimeMillis() - this.lastFlag >= 60000L) {
                if (movementViolations > Config.IMPROBABLE_B_MAX_VIOLATIONS) {
                    this.fail("movementViolations=" + movementViolations);
                    this.lastFlag = System.currentTimeMillis();
                }
            }
        }
    }
}
