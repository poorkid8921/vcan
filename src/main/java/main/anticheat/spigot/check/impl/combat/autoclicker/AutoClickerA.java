package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'A', complexType = "Limit", description = "Left clicking too quickly.")
public class AutoClickerA extends AbstractCheck {
    private final Deque<Long> samples;
    private long lastSwing;

    public AutoClickerA(final PlayerData data) {
        super(data);
        this.samples = Lists.newLinkedList();
        this.lastSwing = 0L;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !this.isExempt(ExemptType.AUTOCLICKER_NON_DIG)) {
            final long now = System.currentTimeMillis();
            final long delay = now - this.lastSwing;
            this.samples.add(delay);
            if (this.samples.size() == 20) {
                final double cps = MathUtil.getCps(this.samples);
                if (cps > Config.AUTOCLICKER_A_MAX_CPS && cps > 5.0) {
                    this.fail("cps=" + cps);
                }
                this.samples.clear();
            }
            this.lastSwing = now;
        }
    }
}
