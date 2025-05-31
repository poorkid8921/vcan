package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'C', complexType = "Rounded", description = "Rounded CPS values.")
public class AutoClickerC extends AbstractCheck {
    private final Deque<Long> samples;

    public AutoClickerC(final PlayerData data) {
        super(data);
        this.samples = Lists.newLinkedList();
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !this.isExempt(ExemptType.AUTOCLICKER)) {
            final long delay = this.data.getClickProcessor().getDelay();
            if (delay > 5000L) {
                this.samples.clear();
                return;
            }
            this.samples.add(delay);
            if (this.samples.size() == 20) {
                final double cps = MathUtil.getCps(this.samples);
                final double difference = Math.abs(Math.round(cps) - cps);
                if (difference < 0.08) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("difference=" + difference);
                    }
                } else {
                    this.decayBuffer();
                }
                this.samples.clear();
            }
        }
    }
}
