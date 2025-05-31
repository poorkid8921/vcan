package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'P', complexType = "Identical", description = "Identical statistical values.")
public class AutoClickerP extends AbstractCheck {
    private final Deque<Long> samples;
    private double lastDeviation;
    private double lastSkewness;
    private double lastKurtosis;

    public AutoClickerP(final PlayerData data) {
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
            if (this.samples.size() == 15) {
                final double deviation = MathUtil.getStandardDeviation(this.samples);
                final double skewness = MathUtil.getSkewness(this.samples);
                final double kurtosis = MathUtil.getKurtosis(this.samples);
                if (deviation == this.lastDeviation && skewness == this.lastSkewness && kurtosis == this.lastKurtosis) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail();
                    }
                } else {
                    this.resetBuffer();
                }
                this.lastDeviation = deviation;
                this.lastSkewness = skewness;
                this.lastKurtosis = kurtosis;
                this.samples.clear();
            }
        }
    }
}
