package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'H', complexType = "Average Deviation", description = "Similar deviation values.")
public class AutoClickerH extends AbstractCheck {
    private final Deque<Long> samples;
    private double lastDeviation;

    public AutoClickerH(final PlayerData data) {
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
                final double deviation = MathUtil.getStandardDeviation(this.samples);
                final double difference = Math.abs(deviation - this.lastDeviation);
                if (difference < 7.52) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("difference=" + difference);
                    }
                } else {
                    this.decayBuffer();
                }
                this.lastDeviation = deviation;
                this.samples.clear();
            }
        }
    }
}
