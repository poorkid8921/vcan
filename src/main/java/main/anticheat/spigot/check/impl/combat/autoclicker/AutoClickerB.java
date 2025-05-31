package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'B', complexType = "Deviation", description = "Too low standard deviation.")
public class AutoClickerB extends AbstractCheck {
    private final Deque<Long> samples;

    public AutoClickerB(final PlayerData data) {
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
            if (this.samples.size() == 50) {
                final double deviation = MathUtil.getStandardDeviation(this.samples);
                final double average = MathUtil.getAverage(this.samples);
                if (deviation < 167.0) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("deviation=" + deviation + " average=" + average);
                    } else {
                        this.decayBuffer();
                    }
                }
                this.samples.clear();
            }
        }
    }
}
