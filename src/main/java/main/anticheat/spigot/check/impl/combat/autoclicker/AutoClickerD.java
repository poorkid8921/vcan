package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'D', complexType = "Skewness", description = "Too low skewness values.")
public class AutoClickerD extends AbstractCheck {
    private final Deque<Long> samples;

    public AutoClickerD(final PlayerData data) {
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
                final double skewness = MathUtil.getSkewness(this.samples);
                if (skewness < -0.01) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("skewness" + skewness);
                    }
                } else {
                    this.decayBuffer();
                }
                this.samples.clear();
            }
        }
    }
}
