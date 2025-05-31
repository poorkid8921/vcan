package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'M', complexType = "Variance Difference", description = "Similar variance values.")
public class AutoClickerM extends AbstractCheck {
    private final Deque<Long> samples;
    private double lastVariance;

    public AutoClickerM(final PlayerData data) {
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
                final double variance = MathUtil.getVariance(this.samples) / 1000.0;
                final double difference = Math.abs(variance - this.lastVariance);
                if (difference < 5.28) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("difference=" + difference);
                    }
                } else {
                    this.decayBuffer();
                }
                this.lastVariance = variance;
                this.samples.clear();
            }
        }
    }
}
