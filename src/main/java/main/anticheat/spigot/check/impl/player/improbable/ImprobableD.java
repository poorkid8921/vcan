package main.anticheat.spigot.check.impl.player.improbable;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Improbable", type = 'D', complexType = "Auto Clicker", description = "Too many combined autoclicker violations.")
public class ImprobableD extends AbstractCheck {
    private long lastFlag;

    public ImprobableD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !this.isExempt(ExemptType.AUTOCLICKER)) {
            final int autoClickerViolations = this.data.getAutoClickerViolations();
            if (System.currentTimeMillis() - this.lastFlag >= 60000L) {
                if (autoClickerViolations > Config.IMPROBABLE_D_MAX_VIOLATIONS) {
                    this.fail("autoClickerViolations=" + autoClickerViolations);
                    this.lastFlag = System.currentTimeMillis();
                }
            }
        }
    }
}
